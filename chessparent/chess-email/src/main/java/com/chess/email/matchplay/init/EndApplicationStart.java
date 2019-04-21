package com.chess.email.matchplay.init;

import com.chess.common.cheserules.Rule;
import com.chess.common.constant.Constant;
import com.chess.common.enumcodes.GameMessage;
import com.chess.common.util.RuleUtil;
import com.chess.common.vo.*;
import com.chess.email.matchplay.vo.MatchGameInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Auther:huang yuan li
 * @Description: 进程开启  该任务开始匹配用于匹配队友
 * @date: Create in ${TIME} ${DATE}
 */
@Component
@Slf4j
public class EndApplicationStart implements CommandLineRunner {
    @Resource
    RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    AmqpTemplate amqpTemplate;
    @Value("${chess.matchConfirm.key}")
    private String matchConfirm;
    @Value("${chess.matchConfirm.expir}")
    private int expirTime;
    // 匹配线程
    private static ScheduledExecutorService sec = Executors.newSingleThreadScheduledExecutor();
    private int match_Play_againTime = 3;// 间隔几秒匹配一次
    private int overTime = 180000;// 超时时间ms

    @Override
    public void run(String... args) throws Exception {
        log.info("匹配服务器启动，开始匹配玩家对局");
        sec.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                log.info("开始匹配");
                matchProcess(GameMessage.MatchGame.getMessageCode());
                log.info("开始rank");
                matchProcess(GameMessage.RankGame.getMessageCode());
                log.info("本轮结匹配结束");
                //显示列表
                //showGameList();
                //检查超时
                checkOverTime();
            }
        }, 1, match_Play_againTime, TimeUnit.SECONDS);
    }

    protected void matchProcess(int matchType) {
        //查询匹配池
        String key = "";
        if(matchType ==GameMessage.MatchGame.getMessageCode()){
            key = Constant.MATCH_GAME_KEY;
        }
        if(matchType ==GameMessage.RankGame.getMessageCode()){
            key = Constant.RANK_GAME_KEY;
        }
        Set<Object> matchInfos = redisTemplate.boundZSetOps(key).range(0, -1);
        int matchSize = matchInfos.size();
        MatchInfo[] matchInfoPools = new MatchInfo[matchSize > 0 ? matchSize : 1];

        matchInfos.toArray(matchInfoPools);
        log.info("匹配人数{}type{},匹配信息{}", matchType,matchSize, matchInfoPools);
        // 按照分数分布排序
        //遍历
        MatchInfo matchInfo = matchInfoPools[0];
        MatchInfo matchInfoNext = null;
        long time = System.currentTimeMillis();
        for (int i = 1; i < matchSize; i++) {
            matchInfoNext = matchInfoPools[i];
            //判断是否能够匹配成功
            log.info("match连两个人{},{}", matchInfo, matchInfoNext);
            if (match(matchInfo, matchInfoNext, time)) {
                //初始化对局;修改用户在线信息  移除匹配池
                log.info("匹配成功");
                try{
                    matchSuccess(matchInfo, matchInfoNext, matchType);
                }catch(Exception e){
                    log.info("{},{},{}",e.getCause(),e.getMessage(),e.getStackTrace());
                }
                if (i + 1 < matchSize) {
                    matchInfo = matchInfoPools[i + 1];
                    i++;
                }
            } else {
                matchInfo = matchInfoNext;
            }
        }


    }

    //配对成功
    private void matchSuccess(MatchInfo matchInfo, MatchInfo matchInfoNext, int matchType) {
        //初始化对局;修改用户在线信息  移除匹配池
        if(matchType == GameMessage.MatchGame.getMessageCode()){
            redisTemplate.boundZSetOps(Constant.MATCH_GAME_KEY).remove(matchInfo);
            redisTemplate.boundZSetOps(Constant.MATCH_GAME_KEY).remove(matchInfoNext);
        }else if(matchType == GameMessage.RankGame.getMessageCode()){
            redisTemplate.boundZSetOps(Constant.RANK_GAME_KEY).remove(matchInfo);
            redisTemplate.boundZSetOps(Constant.RANK_GAME_KEY).remove(matchInfoNext);
        }
        CheseIndex cheseIndex = new CheseIndex();
        cheseIndex.setMessageCode(GameMessage.ConnecGame.getMessageCode());
        cheseIndex.setUserName(matchInfo.getUserName());
        cheseIndex.setOppUserName(matchInfoNext.getUserName());
        cheseIndex.setRamainTime(expirTime);
        //向redis放入对局失效时间
        MatchGameInfo matchGameInfo = new MatchGameInfo(matchInfoNext.getUserName(), matchType, null);
        MatchGameInfo matchGameInfoNext = new MatchGameInfo(matchInfo.getUserName(), matchType, null);
        redisTemplate.opsForValue().set(matchConfirm + matchInfo.getUserName(), matchGameInfo, expirTime, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(matchConfirm + matchInfoNext.getUserName(), matchGameInfoNext, expirTime, TimeUnit.SECONDS);
        amqpTemplate.convertAndSend("chess.play.exchange", "play.message", cheseIndex);
        log.info("找到对局发送消息");
    }

    //判断两个用户能不能匹配成功
    private boolean match(MatchInfo matchInfo, MatchInfo next, long time) {
        //判断是否还在匹配中
        Integer mi = (Integer) redisTemplate.opsForValue().get(Constant.KEEP_ALIVE + matchInfo.getUserName());
        Integer nt = (Integer) redisTemplate.opsForValue().get(Constant.KEEP_ALIVE + next.getUserName());
        if (mi != Constant.LINE_MATCH) {
            //用户不在匹配队列
            log.info("用户{}断开哦",matchInfo.getUserName());
            redisTemplate.boundZSetOps(Constant.MATCH_GAME_KEY).remove(matchInfo);
            return false;
        }
        double grade = Math.abs(matchInfo.getGrade() - next.getGrade());
        int maTime = (int) (time - matchInfo.getDate().getTime());
        maTime = maTime / 1000;
        double floatSorce = getFloatSorce(maTime, matchInfo.getGrade());
        if (floatSorce >= grade) {
            return true;
        }
        //若第一个不满组匹配时间计算第二个
        int nextTime = (int) (time - next.getDate().getTime());
        nextTime = nextTime / 1000;
        double floatSorce1 = getFloatSorce(nextTime, next.getGrade());
        if (floatSorce1 >= grade) {
            return true;
        }
        log.info("浮动分数太小");
        return false;
    }

    //根据时间返回匹配当前浮动分
    private double getFloatSorce(int waitTime, double sorce) {
        if (waitTime <= 10) {
            return sorce;
        } else if (waitTime <= 20) {
            // 增加分数段1.5倍
            return (double) (1.5 * sorce);
        } else if (waitTime <= 40) {
            return 2 * sorce;
        } else if (waitTime <= 80) {
            return 3 * sorce;
        } else {
            return 4 * sorce;
        }
    }

    //检查超时下棋
    private void checkOverTime(){
        //获取轮到谁下棋的集合
        Set members = redisTemplate.boundSetOps(Constant.REMAN_TIME).members();
        Iterator iterator = members.iterator();
        Instant now = Instant.now();
        while (iterator.hasNext()){
            RemanTimeVO remanTimeVO = (RemanTimeVO) iterator.next();
            boolean after = remanTimeVO.getOverTime().isAfter(now);
            if(!after){
                //下棋超时自动走位并发送消息
                goAutomatic(remanTimeVO);
            }
        }
    }

    //自动走位
    private void goAutomatic(RemanTimeVO remanTimeVO) {
        //删除
        redisTemplate.boundSetOps(Constant.REMAN_TIME).remove(remanTimeVO);
        redisTemplate.delete(Constant.SINGLE_OVER_TIME + remanTimeVO.getUserName());
        //获取棋盘信息
        CheckerBoardInfo checkerBoardInfo = (CheckerBoardInfo) redisTemplate.opsForValue().get(Constant.CHECKBOARD_INFO + remanTimeVO.getUserName());
        String checkerBoardID = checkerBoardInfo.getCheckerBoardID();
        int[][] cheses = (int[][]) redisTemplate.opsForValue().get(Constant.CHECKERBOARD_REDIS_ID + checkerBoardID);
        CodeIndex codeIndex = Rule.goAutomic(cheses, checkerBoardInfo.getCode());
        //改变数组
        cheses[codeIndex.getEndX()][codeIndex.getEndY()] = codeIndex.getCode();
        cheses[codeIndex.getStartX()][codeIndex.getStartY()] = 0;
        redisTemplate.opsForValue().set(Constant.CHECKERBOARD_REDIS_ID + checkerBoardID, cheses);
        //发送mq消息
        CheseIndex cheseIndex = new CheseIndex();
        cheseIndex.setMessageCode(GameMessage.AUTOCHESEMOVE.getMessageCode());
        cheseIndex.setMessage(GameMessage.AUTOCHESEMOVE.getMessage());
        cheseIndex.setCodeIndex(codeIndex);
        cheseIndex.setTurnMe(checkerBoardInfo.getOppUserName());
        cheseIndex.setUserName(remanTimeVO.getUserName());
        cheseIndex.setOppUserName(checkerBoardInfo.getOppUserName());
        cheseIndex.setRamainTime(60);
        cheseIndex.setGameState(GameMessage.PlayIng.getMessageCode());
        redisTemplate.opsForValue().set(Constant.SINGLE_OVER_TIME + checkerBoardInfo.getOppUserName(),
                Instant.now().plusSeconds(60));
        RemanTimeVO timeVO = new RemanTimeVO();
        timeVO.setOverTime(Instant.now().plusSeconds(60));
        timeVO.setUserName(checkerBoardInfo.getOppUserName());
        redisTemplate.boundSetOps(Constant.REMAN_TIME).add(timeVO);
        redisTemplate.opsForValue().set(Constant.SINGLE_OVER_TIME + checkerBoardInfo.getOppUserName()
                , Instant.now().plusSeconds(60));
        amqpTemplate.convertAndSend("chess.play.exchange", "play.message", cheseIndex);


    }

}
