package com.chess.email.matchplay.init;

import com.chess.common.constant.Constant;
import com.chess.common.enumcodes.GameMessage;
import com.chess.common.vo.CheseIndex;
import com.chess.common.vo.MatchInfo;
import com.chess.email.matchplay.task.MyRunable;
import com.chess.email.matchplay.vo.MatchGameInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.ExecutorService;
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
    private int match_Play_againTime = 30;// 间隔几秒匹配一次
    private int overTime = 180000;// 超时时间ms

    @Override
    public void run(String... args) throws Exception {
        log.info("匹配服务器启动，开始匹配玩家对局");
        sec.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                log.info("开始匹配");
                matchProcess();
                log.info("开始rank");
                //matchRank();
                log.info("本轮结匹配结束");
                //显示列表
                //showGameList();
            }
        }, 1, match_Play_againTime, TimeUnit.SECONDS);
    }

    protected void matchProcess() {
        //查询匹配池
        Set<Object> matchInfos = redisTemplate.boundZSetOps(Constant.MATCH_GAME_KEY).range(0, -1);
        int matchSize = matchInfos.size();
        MatchInfo[] matchInfoPools = new MatchInfo[matchSize > 0 ? matchSize : 1];

        matchInfos.toArray(matchInfoPools);
        log.info("匹配人数{},匹配信息{}", matchSize, matchInfoPools);
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
                matchSuccess(matchInfo, matchInfoNext);
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
    private void matchSuccess(MatchInfo matchInfo, MatchInfo matchInfoNext) {
        //初始化对局;修改用户在线信息  移除匹配池
        redisTemplate.boundZSetOps(Constant.MATCH_GAME_KEY).remove(matchInfo);
        redisTemplate.boundZSetOps(Constant.MATCH_GAME_KEY).remove(matchInfoNext);
        redisTemplate.opsForValue().get(Constant.KEEP_ALIVE + matchInfo.getUserName());
        redisTemplate.opsForValue().get(Constant.KEEP_ALIVE + matchInfoNext.getUserName());
        CheseIndex cheseIndex = new CheseIndex();
        cheseIndex.setMessageCode(GameMessage.ConnecGame.getMessageCode());
        cheseIndex.setUserName(matchInfo.getUserName());
        cheseIndex.setOppUserName(matchInfoNext.getUserName());
        //向redis放入对局失效时间
        MatchGameInfo matchGameInfo = new MatchGameInfo(matchInfoNext.getUserName(), 0, null);
        MatchGameInfo matchGameInfoNext = new MatchGameInfo(matchInfo.getUserName(), 0, null);
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
        if (mi != 1) {
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

}
