package com.chess.play;

import com.alibaba.fastjson.JSON;
import com.chess.client.FriendClient;
import com.chess.common.constant.Constant;
import com.chess.common.enumcodes.GameMessage;
import com.chess.common.util.Msg;
import com.chess.common.vo.CheckerBoardInfo;
import com.chess.common.vo.CheseIndex;
import com.chess.common.vo.MatchInfo;
import com.chess.service.PlayService;
import com.chess.user.pojo.Friend;
import com.chess.vo.MatchMessage;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class WsHandler extends TextWebSocketHandler {

    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    public static Map<String, String> sessionIds = new ConcurrentHashMap<>();
    public static Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    public static Map<String, Double> matchInfos = new ConcurrentHashMap<>();

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private FriendClient friendClient;

    @Autowired
    private PlayService playService;
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 删除在线信息
        redisTemplate.delete(Constant.KEEP_ALIVE + sessionIds.get(session.getId()));
        sessionMap.remove(sessionIds.get(session.getId()));
        sessionIds.remove(session.getId());
        session.getId();
        subOnlineCount();
        // 判断匹配对局中
        log.info("断开连接");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        HttpHeaders handshakeHeaders = session.getHandshakeHeaders();
        String userName = handshakeHeaders.get("username").get(0);
        addOnlineCount(); // 在线数加1
        //查询是否有对局为完成
        CheckerBoardInfo checkerBoardInfo = (CheckerBoardInfo) redisTemplate.opsForValue()
                .get(Constant.CHECKBOARD_INFO + userName);
        if (checkerBoardInfo != null) {
            //存在对局
            playService.againConnectMatch(checkerBoardInfo,session,userName);
        }else{
            firstConnect(session,userName);
        }
        sessionIds.put(session.getId(),userName);
        sessionMap.put(userName,session);

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("接受消息");
        String msg = message.getPayload();
        log.info(msg);
        CheseIndex cheseIndex = JSON.toJavaObject(JSON.parseObject(msg), CheseIndex.class);
        log.info("cheseindex:{}", cheseIndex);
        int messageCode = cheseIndex.getMessageCode();
        String userName = sessionIds.get(session.getId());
        if(messageCode == GameMessage.MatchGame.getMessageCode()){
            matchGame(userName, session);
            return;
        }else if(messageCode == GameMessage.RemoveMatch.getMessageCode()){
            //取消匹配
            removeMatch(userName, session.getId());
            return;
        }else if(messageCode == GameMessage.ChesesMove.getMessageCode()){
            //移动棋子
            playService.cheseMove(session,cheseIndex);
        }

    }

    //匹配
    private void matchGame(String userName,WebSocketSession session){
        //匹配对局
        //获取rank分值
        double rankGrade=0;
        matchInfos.put(userName,rankGrade);
        MatchInfo matchInfo = new MatchInfo(userName, rankGrade, new Date());
        redisTemplate.boundZSetOps(Constant.MATCH_GAME_KEY).add(matchInfo,rankGrade);
        //设置用户状态
        redisTemplate.opsForValue().set(Constant.KEEP_ALIVE + userName,1);
        CheseIndex cheseIndex = new CheseIndex();
        cheseIndex.setMessage(GameMessage.MatchIng.getMessage());
        cheseIndex.setMessageCode(GameMessage.MatchIng.getMessageCode());
        sendMessage(session,cheseIndex);
    }
    //移除匹配队列
    private void removeMatch(String userName, String sessionId){
        //移除匹配队列
        redisTemplate.boundZSetOps(Constant.MATCH_GAME_KEY).remove(matchInfos.get(userName));
        //设置用户状态
        redisTemplate.opsForValue().set(Constant.KEEP_ALIVE + userName,0);
        matchInfos.remove(userName);
        //移除成功
        CheseIndex cheseIndex = new CheseIndex();
        cheseIndex.setMessageCode(GameMessage.RemoveMatchSucess.getMessageCode());
        cheseIndex.setMessage(GameMessage.RemoveMatchSucess.getMessage());
        sendMessage(sessionMap.get(sessionId),cheseIndex);
    }
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WsHandler.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WsHandler.onlineCount--;
    }
    private void firstConnect(WebSocketSession session, String userName){
        //设置用户状态
        redisTemplate.opsForValue().set(Constant.KEEP_ALIVE + userName,0);
        sessionIds.put(session.getId(), userName);
        sessionMap.put(userName, session);
        // 显示好友
        Msg msgFriends = friendClient.getFriends(userName);
        log.info("服务内部调用");
        log.info("好友信息{}",msgFriends);
        Map<String, Object> friends = null;
        if(msgFriends.getCode() == 200 && msgFriends.getData() != null && msgFriends.getData().size()!=0){
            friends = msgFriends.getData();
        }
        //输出好友信息
        CheseIndex cheseIndex = new CheseIndex();
        cheseIndex.setMessageCode(GameMessage.ShowFriends.getMessageCode());
        cheseIndex.setMessage(GameMessage.ShowFriends.getMessage());
        cheseIndex.setMap(friends);
        sendMessage(session,cheseIndex);
        log.info("有新连接加入！当前在线人数为" + getOnlineCount());
        log.info("当前接入的用户名称是{} sessionid{}", userName, session.getId());
    }

    public void sendMessage(WebSocketSession session, Object object) {
        String jsonString = JSON.toJSONString(object);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(jsonString));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                log.error("发送失败");
            }
        }
    }

}
