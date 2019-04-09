package com.chess.email.matchplay.service;

import com.chess.common.enumcodes.ExceptionEnum;
import com.chess.common.exception.ChessException;
import com.chess.common.util.Msg;
import com.chess.email.matchplay.constant.MatchConstant;
import com.chess.email.matchplay.task.MyRunable;
import com.chess.email.matchplay.vo.MatchGameInfo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    //chushi初始化线程池执行对局初始化
    ExecutorService initGameSec = Executors.newCachedThreadPool();
    @Autowired
    private AmqpTemplate amqpTemplate;
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
            initGameSec.submit(new MyRunable(redisTemplate,amqpTemplate,matchGameInfo.getMatchType(),userName,otherUserName));
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
        return null;
    }
}
