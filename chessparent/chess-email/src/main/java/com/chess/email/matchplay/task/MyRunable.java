package com.chess.email.matchplay.task;

import com.chess.common.constant.Constant;
import com.chess.common.enumcodes.GameMessage;
import com.chess.common.util.CheseCode;
import com.chess.common.util.RuleUtil;
import com.chess.common.vo.CheckerBoardInfo;
import com.chess.common.vo.CheseIndex;
import com.chess.common.vo.RemanTimeVO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * @Auther: huang yuan li
 * @Description: 匹配任务
 * @date: Create in 下午 2:01 2019/3/31 0031
 * @Modifide by:
 */
@Slf4j
@Component
@NoArgsConstructor
@AllArgsConstructor
public class MyRunable implements Runnable {

    RedisTemplate<Object, Object> redisTemplate;
    AmqpTemplate amqpTemplate;
    private int matchType;
    private String userNameRed;
    private String userNameBack;

    @Override
    public void run() {
        log.info("查看注入的redis和amq{},{}",redisTemplate,amqpTemplate);
        log.info("初始化棋盘类型{}", matchType);
        // 初始化棋盘
        int[][] initCheckerboard = RuleUtil.initCheckerboard();
        // 棋盘数据存入redis中
        String checkerboardid = Constant.CHECKERBOARD_REDIS_ID + UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(Constant.CHECKERBOARD_REDIS_ID + checkerboardid, initCheckerboard);
        // 一方执红棋，一方白旗
        CheckerBoardInfo info = new CheckerBoardInfo();
        info.setCheckerBoardID(checkerboardid);
        info.setCode(CheseCode.Red.getCode());
        info.setGameState(GameMessage.PlayIng.getMessageCode());
        info.setName(CheseCode.Red.getCheseName());
        info.setType(matchType);
        info.setOppUserName(userNameBack);
        info.setDate(new Date());
        CheckerBoardInfo info1 = new CheckerBoardInfo();
        info1.setCheckerBoardID(checkerboardid);
        info1.setGameState(GameMessage.PlayIng.getMessageCode());
        info1.setCode(CheseCode.Back.getCode());
        info1.setName(CheseCode.Back.getCheseName());
        info1.setType(matchType);
        info1.setDate(new Date());
        // info1.setOppNickName(oppNickName);
        info1.setOppUserName(userNameRed);
        //将信息存入缓存中
        redisTemplate.opsForValue().set("turnMe:" + checkerboardid, userNameRed);
       try{
           redisTemplate.opsForValue().set(Constant.SINGLE_OVER_TIME + userNameRed, Instant.now().plusSeconds(60));
           RemanTimeVO remanTimeVO = new RemanTimeVO();
           remanTimeVO.setUserName(userNameRed);
           remanTimeVO.setOverTime(Instant.now().plusSeconds(64));
           remanTimeVO.setTime(64);
           redisTemplate.boundSetOps(Constant.REMAN_TIME).add(remanTimeVO);
       }catch (Exception e){
           System.out.println("异常"+e);
       }
        redisTemplate.opsForValue().set(Constant.CHECKBOARD_INFO + userNameRed, info);
        redisTemplate.opsForValue().set(Constant.CHECKBOARD_INFO + userNameBack, info1);
        // 棋盘初始化成功向mq发消息
        CheseIndex cheseIndex = new CheseIndex();
        cheseIndex.setMessageCode(GameMessage.InitGame.getMessageCode());
        cheseIndex.setMessage(GameMessage.InitGame.getMessage());
        cheseIndex.setUserName(userNameRed);
        cheseIndex.setRamainTime(60);
        cheseIndex.setOppUserName(userNameBack);
        cheseIndex.setRedUserName(userNameRed);
        cheseIndex.setTurnMe(userNameRed);
        cheseIndex.setGameState(GameMessage.PlayIng.getMessageCode());
        HashMap<String, Object> map = new HashMap<>();
        map.put("cheses",initCheckerboard);
        map.put("startTime",Instant.now());
        cheseIndex.setMap(map);

        //发送消息
        log.info("初始化成准备发送mq消息");
        amqpTemplate.convertAndSend("chess.play.exchange", "play.message", cheseIndex);
        log.info("发送mq消息成功");
    }
}