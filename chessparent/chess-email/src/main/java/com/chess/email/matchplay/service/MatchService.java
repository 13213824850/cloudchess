package com.chess.email.matchplay.service;

import com.chess.common.constant.Constant;
import com.chess.common.constant.MQConstant;
import com.chess.common.enumcodes.ExceptionEnum;
import com.chess.common.enumcodes.GameMessage;
import com.chess.common.exception.ChessException;
import com.chess.common.util.Msg;
import com.chess.common.vo.CheseIndex;
import com.chess.email.client.GameListClient;
import com.chess.email.matchplay.constant.MatchConstant;
import com.chess.email.matchplay.task.MyRunable;
import com.chess.email.matchplay.vo.MatchGameInfo;
import com.chess.rankhis.enty.GameList;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 4:02 2019/4/2 0002
 * @Modifide by:
 */
@Service
public class MatchService {
    @Value("${chess.matchConfirm.key}")
    private String matchConfirm;
    @Value("${chess.matchConfirm.expir}")
    private int expir;
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    //chushi初始化线程池执行对局初始化
    ExecutorService initGameSec = Executors.newCachedThreadPool();
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private GameListClient gameListClient;

    public Msg confirmMatch(String userName){
        //查询是否存在对局
        //判断另一个玩家是否已经确定且为超时
        MatchGameInfo matchGameInfo = (MatchGameInfo) redisTemplate.opsForValue().get(matchConfirm + userName);
        if(matchGameInfo == null){
            throw new ChessException(ExceptionEnum.MATCHOVERTIME);
        }
        String state = matchGameInfo.getUserNameState();
        if(state != null){
            return Msg.success();
        }
        String otherUserName = matchGameInfo.getOtherUserName();
        MatchGameInfo matchGameInfoOther = (MatchGameInfo) redisTemplate.opsForValue().get(matchConfirm + otherUserName);
        if(matchGameInfoOther == null){
            //另一个玩家拒绝
            throw new ChessException(ExceptionEnum.REFUSE_MATCH);
        }
        if(MatchConstant.AGREE_MATCH.equals(matchGameInfoOther.getUserNameState())){
            //另一个玩家以同意,提交对局任务
            initGameSec.submit(new MyRunable(redisTemplate,amqpTemplate,matchGameInfo.getMatchType(),userName,otherUserName, gameListClient));
            return Msg.success();
        }
        //同意对局
        matchGameInfo.setUserNameState(MatchConstant.AGREE_MATCH);
        redisTemplate.opsForValue().set(matchConfirm+userName, matchGameInfo);
        return Msg.success();
    }

    public Msg refuseGame(String userName) {
        redisTemplate.delete(matchConfirm + userName);
        return Msg.success();
    }

    public Msg lauchPlay(String userName, String oppUserName) {
        //判断是否是在线状态
        int onLine = (int) redisTemplate.opsForValue().get(Constant.KEEP_ALIVE + userName);
        int oppOnLine = (int) redisTemplate.opsForValue().get(Constant.KEEP_ALIVE + oppUserName);
        if(onLine == Constant.LINE_ON && Constant.LINE_ON== oppOnLine){
            //发送约战消息给好友
            CheseIndex cheseIndex = new CheseIndex();
            cheseIndex.setMessageCode(GameMessage.FriendsGame.getMessageCode());
            cheseIndex.setMessage(GameMessage.FriendsGame.getMessage());
            //接收方
            cheseIndex.setUserName(oppUserName);
            cheseIndex.setOppUserName(userName);
            cheseIndex.setRamainTime(expir);
            amqpTemplate.convertAndSend(MQConstant.CHESEINDEX_EXCHANGE,MQConstant.CHESEINDEX_KEY,cheseIndex);
            //设置好友确认过期时间
            redisTemplate.opsForValue().set(matchConfirm + oppUserName, userName, expir, TimeUnit.SECONDS);
            return Msg.success();
        }
        //不成功原因
        if(onLine == Constant.LINE_OFF){
            throw new ChessException(ExceptionEnum.CUT_SERVER);
        }
        if(onLine == Constant.LINE_MATCH){
            throw new ChessException(ExceptionEnum.WE_PLAYING);
        }
        if(oppOnLine == Constant.LINE_OFF){
            throw new ChessException(ExceptionEnum.NOT_ONLINE);
        }
        if(oppOnLine == Constant.LINE_MATCH){
            throw new ChessException(ExceptionEnum.FRIEND_MATCH);
        }
        if(oppOnLine == Constant.LINE_PLAY){
            throw new ChessException(ExceptionEnum.FRIEND_PLAYING);
        }
        return null;
    }

    //好友同意
    public Msg friendAgree(String userName) {
        //判断是否超时
        Object state = redisTemplate.opsForValue().get("matchConfirm" + userName);
        if(state == null){
            throw new ChessException(ExceptionEnum.CONFIRM_OVER_TIME);
        }
        String oppUserName = (String) state;
        //初始化对局
        initGameSec.submit(new MyRunable(redisTemplate,amqpTemplate,GameMessage.FriendsGame.getMessageCode(),userName,oppUserName, gameListClient));
        return Msg.success();
    }

    //好友拒绝
    public Msg friendRefuse(String userName) {
        redisTemplate.delete("matchConfirm"+userName);
        return Msg.success();
    }
}
