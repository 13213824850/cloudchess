package com.chess.mqlisten;

import com.alibaba.fastjson.JSON;
import com.chess.common.constant.Constant;
import com.chess.common.enumcodes.GameMessage;
import com.chess.common.vo.CheseIndex;
import com.chess.play.WsHandler;
import com.chess.user.pojo.Friend;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: huang yuan li
 * @Description: 监听mq消息推送至客户端
 * @date: Create in 下午 8:42 2019/3/22 0022
 * @Modifide by:
 */

@Component
@Slf4j
public class ChessMessage {

    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "chess.play.message"),
            exchange = @Exchange(name = "chess.play.exchange", type = ExchangeTypes.DIRECT),
            key = "play.message"))
    public void listenPlayMessage(CheseIndex cheseIndex) {
        log.info("接收mq信息cheseIndex{}", cheseIndex);
        if (cheseIndex == null) {
            return;
        }
        String userName = cheseIndex.getUserName();
        String userNameOpp = cheseIndex.getOppUserName();
        int code = cheseIndex.getMessageCode();
        if (code == GameMessage.InitGame.getMessageCode() || code == GameMessage.ConnecGame.getMessageCode()) {
            //初始化
            log.info("初始化");
            sendMessage(WsHandler.sessionMap.get(userName), cheseIndex);
            sendMessage(WsHandler.sessionMap.get(userNameOpp), cheseIndex);
            return;
        }
        if (code == GameMessage.ChesesMove.getMessageCode()) {
            log.info("移动棋子");
            sendMessage(WsHandler.sessionMap.get(cheseIndex.getTurnMe()), cheseIndex);
            return;
        }
        if (code == GameMessage.AUTOCHESEMOVE.getMessageCode()) {
            sendMessage(WsHandler.sessionMap.get(userName), cheseIndex);
            sendMessage(WsHandler.sessionMap.get(userNameOpp), cheseIndex);
            return;
        }
        if (code == GameMessage.UPDATE_SHOW_FRIENDS.getMessageCode()) {
            List<String> friends = (List<String>) cheseIndex.getMap().get("friends");
            int onLine = (int) cheseIndex.getMap().get("onLine");
            CheseIndex sendCheseIndex = new CheseIndex();
            sendCheseIndex.setMessageCode(cheseIndex.getMessageCode());
            sendCheseIndex.setMessage(cheseIndex.getMessage());
            sendCheseIndex.add("onLine", onLine).add("friend", userName);
            //发送好友在线信息变更消息
            for (int i = 0; i < friends.size(); i++) {
                sendMessage(WsHandler.sessionMap.get(friends.get(i)), sendCheseIndex);
            }
            return;
        }
        //判断是否是添加好友请求
        if (code == GameMessage.RECEIVE_LAUNCH_MESSAGE.getMessageCode()) {
            sendMessage(WsHandler.sessionMap.get(userNameOpp), cheseIndex);
            return;
        }
        //接受好友对战请求
        if (code == GameMessage.FriendsGame.getMessageCode()) {
            sendMessage(WsHandler.sessionMap.get(userName), cheseIndex);
            return;
        }

        //sh删除好友消息
        if (code == GameMessage.DELETE_SINGLE_FRIEND.getMessageCode()) {
            sendMessage(WsHandler.sessionMap.get(userNameOpp), cheseIndex);
            return;
        }
        //好友添加成功 更新好友
        if (code == GameMessage.FRIEND_ADD_SUCCESS.getMessageCode()) {
            sendMessage(WsHandler.sessionMap.get(userName), cheseIndex);
            return;
        }
        //发送更新对局列表
        if (code == GameMessage.ADD_GAME_LIST.getMessageCode() || code == GameMessage.DELETE_GAME_LIST.getMessageCode()) {
            Map<String, WebSocketSession> sessionMap = WsHandler.sessionMap;
            for (String name : sessionMap.keySet()) {
                Object o = redisTemplate.opsForValue().get(Constant.KEEP_ALIVE + name);
                if (o != null && (int) o == 1) {
                    sendMessage(sessionMap.get(name), cheseIndex);
                }
            }
            return;
        }
        if (code == GameMessage.WATCH_PLAY_MOVE.getMessageCode()) {
            String id = cheseIndex.getCheckBoardInfoId();
            log.info("{}redis:", redisTemplate);
            Set members = redisTemplate.boundSetOps(id).members();
            if (members == null || members.size() == 0) {
                return;
            }
            Iterator iterator = members.iterator();
            while ( iterator.hasNext()) {
                String next = (String) iterator.next();
                WebSocketSession session = WsHandler.sessionMap.get(next);
                if(session == null){
                    redisTemplate.boundSetOps(id).remove(next);
                    continue;
                }
                sendMessage(session,cheseIndex);
            }
            return;
        }
        return;
    }

    private void sendMessage(WebSocketSession session, CheseIndex cheseIndex) {
        if (session != null && session.isOpen()) {
            String s = JSON.toJSONString(cheseIndex);
            try {
                session.sendMessage(new TextMessage(s));
            } catch (IOException e) {
                e.printStackTrace();
                log.error("发送失败{}", s);
            }
        }
    }
}
