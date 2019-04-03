package com.chess.play;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import com.chess.cheserules.Rule;
import com.chess.common.constant.Constant;
import com.chess.common.enumcodes.GameMessage;
import com.chess.common.util.CheseCode;
import com.chess.common.util.Msg;
import com.chess.common.util.RuleUtil;
import com.chess.common.vo.CheckerBoardInfo;
import com.chess.common.vo.CheseIndex;
import com.chess.common.vo.CodeIndex;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GameStartWs extends TextWebSocketHandler {
    // 静态变量，用来记录当前游戏连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    public static Map<String, String> sessionIds = new ConcurrentHashMap<>();
    // 存放游戏中的玩家
    public static Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionMap.remove(sessionIds.get(session.getId()));
        sessionIds.remove(session.getId());
        session.getId();
        // 删除在线信息
        redisTemplate.delete(Constant.KEEP_ALIVE + sessionIds.get(session.getId()));
        subOnlineCount();
        log.info("断开连接");
    }

    //建立对局连接并初始化棋盘
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        HttpHeaders handshakeHeaders = session.getHandshakeHeaders();
        String userName = handshakeHeaders.get("username").get(0);
        addOnlineCount(); // 在线数加1
        addOnlineCount(); // 在线数加1
        //设置用户状态
        redisTemplate.opsForValue().set(Constant.KEEP_ALIVE + userName,2);
        sessionIds.put(session.getId(), userName);
        sessionMap.put(userName, session);
        log.info("当前接入的id是{} 人数{}", userName, onlineCount);
        // 先检查是否存在对局若已存在 则存在则初始化 否则断开连接
        CheckerBoardInfo checkerBoardInfo = (CheckerBoardInfo) redisTemplate.opsForValue()
                .get(Constant.CHECKBOARD_INFO + userName);
        if (checkerBoardInfo == null) {
            session.close();
        }
        //获取棋盘信息
        int[][] cheses = (int[][]) redisTemplate.opsForValue()
                .get(Constant.CHECKERBOARD_REDIS_ID + checkerBoardInfo.getCheckerBoardID());
        CheseIndex cheseIndex = new CheseIndex();
        cheseIndex.setMessageCode(GameMessage.InitGame.getMessageCode());
        String turnMe = (String) redisTemplate.opsForValue()
                .get("turnMe:" + checkerBoardInfo.getCheckerBoardID());
        cheseIndex.add("checkerBoardInfo", checkerBoardInfo).add("cheses", cheses);
        cheseIndex.setTurnMe(turnMe);
        cheseIndex.setGameState(checkerBoardInfo.getGameState());
        cheseIndex.setRedUserName(checkerBoardInfo.getCode() == CheseCode.Red.getCode() ? userName : checkerBoardInfo.getOppUserName());
        sendMessage(session, new TextMessage(JSON.toJSONString(cheseIndex)));
        log.info("建立连接 开始对局棋盘{}", JSON.toJSONString(cheseIndex));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("接受消息");
        String msg = message.getPayload();
        log.info(msg);
        CheseIndex cheseIndex = JSON.toJavaObject(JSON.parseObject(msg), CheseIndex.class);
        log.info("cheseindex:{}", cheseIndex);
        // 判断消息类型
        GameMessage gm = GameMessage.matchCode(cheseIndex.getMessageCode());
        switch (gm) {
            case ChesesMove:
                cheseMove(session, cheseIndex);
                break;// 移动棋子
            case LaunchFriend:
                //添加好友
               // launchFriend(session.getId());
                break;
            default:
                break;
        }
        return;

    }

    /*//好友添加
    private void launchFriend(String id) {
        //查询对方id
        String userName = sessionIds.get(id);
        CheckerBoardInfo checkerboardinfo = (CheckerBoardInfo) redisTemplate.opsForValue()
                .get(Constant.CHECKBOARD_INFO + userName);
        String oppUserName = checkerboardinfo.getOppUserName();
        Msg addMessage = friendLaunchMessageService.addMessage(userName, oppUserName);
        if (addMessage.getCode() == 200) {
            CheseIndex cheseIndex = new CheseIndex();
            cheseIndex.setMessageCode(GameMessage.ResiveLaunchFriend.getMessageCode());
            Map<String, Object> map = new HashMap<>();
            map.put("launchUser", addMessage.getData().get("launchUserInfo"));
            cheseIndex.setMap(map);
            TextMessage textMessage = new TextMessage(JSON.toJSONString(cheseIndex));
            sendMessage(sessionMap.get(oppUserName), textMessage);
            sendMessage(WsHandler.sessionMap.get(oppUserName), textMessage);
        }

    }*/

    private void cheseMove(WebSocketSession session, CheseIndex cheseIndex) throws IOException {
        // 获取棋盘
        String userName = sessionIds.get(session.getId());
        CheckerBoardInfo checkerboardinfo = (CheckerBoardInfo) redisTemplate.opsForValue()
                .get(Constant.CHECKBOARD_INFO + userName);
        TextMessage sendMsg = null;
        // 检查游戏状态如果游戏为结束则结束
        if (checkerboardinfo.getGameState() == GameMessage.RedWin.getMessageCode()
                || checkerboardinfo.getGameState() == GameMessage.BackWin.getMessageCode()) {
            cheseIndex.setMessage("游戏结束");
            cheseIndex.setGameState(checkerboardinfo.getGameState());
            cheseIndex.setMessageCode(checkerboardinfo.getGameState());
            sendMsg = new TextMessage(JSON.toJSONString(cheseIndex));
            sendMessage(session, sendMsg);
            //若rank结算
           /* if (checkerboardinfo.getType() == GameMessage.RankGame.getMessageCode()) {
                rankService.winGameRank(userName);
                rankService.transportGameRank(checkerboardinfo.getOppUserName());
            }*/
           return;
        }
        // 开始检查棋子移动规则
        gamePlaying(checkerboardinfo, cheseIndex, session);


    }

    //棋子移动
    private void gamePlaying(CheckerBoardInfo checkerboardinfo, CheseIndex cheseIndex, WebSocketSession session) {
        String checkRule = checkRule(checkerboardinfo, cheseIndex);
        // 游戏状态
        int gameState;
        String[] split = checkRule.split(":");
        if (split.length == 2) {
            gameState = Integer.parseInt(split[1]);
            gameResullt(cheseIndex, checkerboardinfo, gameState, session);
        } else {
            // 棋子移动不符合规则 只发送给自己消息
            String turnMe = sessionIds.get(session.getId());
            turnMe = (String) redisTemplate.opsForValue().get("turnMe:" + checkerboardinfo.getCheckerBoardID());
            cheseIndex.setMessageCode(GameMessage.CheseMoveErr.getMessageCode());
            cheseIndex.setMessage(GameMessage.CheseMoveErr.getMessage());
            cheseIndex.setTurnMe(turnMe);
            TextMessage sendMsg = new TextMessage(JSON.toJSONString(cheseIndex));
            sendMessage(session, sendMsg);
        }

    }

    private void gameResullt(CheseIndex cheseIndex, CheckerBoardInfo checkerboardinfo, int gameState,
                             WebSocketSession session) {
        String userName = sessionIds.get(session.getId());
        if (gameState == GameMessage.BackWin.getMessageCode() || gameState == GameMessage.RedWin.getMessageCode()) {
            log.debug("游戏结束gameState{}", gameState);
            // 清楚数据
            cheseIndex.setGameState(gameState);
            checkerboardinfo.setGameState(gameState);
            redisTemplate.delete(Constant.CHECKBOARD_INFO + userName);
            redisTemplate.delete(Constant.CHECKBOARD_INFO + checkerboardinfo.getOppUserName());
            //redisTemplate.opsForValue().set(Constant.CHECKBOARD_INFO + userName, checkerboardinfo, 30,
            //		TimeUnit.MINUTES);
            //CheckerBoardInfo infop = checkerboardinfo;
            //infop.setOppUserName(userName);
            //redisTemplate.opsForValue().set(Constant.CHECKBOARD_INFO + checkerboardinfo.getOppUserName(), infop, 30,
            //		TimeUnit.MINUTES);
            // 棋盘信息
            redisTemplate.delete("turnMe:" + checkerboardinfo.getCheckerBoardID());
            redisTemplate.delete(Constant.CHECKERBOARD_REDIS_ID + checkerboardinfo.getCheckerBoardID());
            //redisTemplate.expire("turnMe:" + infop.getCheckerBoardID(), 30, TimeUnit.MINUTES);
            //redisTemplate.expire(Constant.CHECKERBOARD_REDIS_ID + infop.getCheckerBoardID(), 30, TimeUnit.MINUTES);
            // 将记录保存到数据库中
           /* GameRecord gameRecord = new GameRecord();
            gameRecord.setWinUserName(userName);
            String baduserName = checkerboardinfo.getOppUserName();
            gameRecord.setType(checkerboardinfo.getType());
            gameRecord.setStansportUserName(baduserName);
            gameRecordService.addGameRecord(gameRecord);*/
        } else {
            cheseIndex.setGameState(GameMessage.PlayIng.getMessageCode());
            // 更改turnMe
            String turnMe = cheseIndex.getUserName() == userName ? cheseIndex.getOppUserName() : cheseIndex.getUserName();
            cheseIndex.setTurnMe(turnMe);
            redisTemplate.opsForValue().set("turnMe:" + checkerboardinfo.getCheckerBoardID(), turnMe);
        }
        // 向对方发送己方所走棋位
        TextMessage sendMsg = new TextMessage(JSON.toJSONString(cheseIndex));
        // 向对方发送数据
        log.debug("对方userName{}", checkerboardinfo.getOppUserName());
        amqpTemplate.convertAndSend("chess.play.exchange", "play.message", cheseIndex);
        sendMessage(session, sendMsg);

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
        GameStartWs.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        GameStartWs.onlineCount--;
    }

    //检查棋子移动规则
    private String checkRule(CheckerBoardInfo checkerboardinfo, CheseIndex cheseIndex) {
        CodeIndex codeIndex = cheseIndex.getCodeIndex();
        if (!RuleUtil.checkOutBoard(codeIndex)) {
            return "请在棋盘内下棋";
        }
        int code = codeIndex.getCode();
        if (!((checkerboardinfo.getCode() == CheseCode.Red.getCode() && code > 0)
                || checkerboardinfo.getCode() == CheseCode.Back.getCode() && code < 0)) {
            return "不符合走棋规则";
        }

        int[][] cheses = (int[][]) redisTemplate.opsForValue()
                .get(Constant.CHECKERBOARD_REDIS_ID + checkerboardinfo.getCheckerBoardID());
        log.info("棋盘{}", cheses);
        // 判断传入的code与目标是否一致
        if (cheses[codeIndex.getStartX()][codeIndex.getStartY()] != code) {
            log.info("传入的棋子code不等{}", code);
            return "不符合走棋规则";
        }
        // 检查棋子走位规则
        boolean checkRules = Rule.checkRules(cheses, codeIndex.getStartX(), codeIndex.getStartY(),
                codeIndex.getEndX(), codeIndex.getEndY());
        if (!checkRules) {
            log.info("Rulle{}", checkRules);
            return "不符合走棋规则";
        }
        // 判断游戏状态
        boolean winGame = RuleUtil.winGame(cheses[codeIndex.getEndX()][codeIndex.getEndY()]);
        int gameState = GameMessage.PlayIng.getMessageCode();
        if (winGame) {
            if (code > 0) {
                gameState = GameMessage.RedWin.getMessageCode();
            } else {
                gameState = GameMessage.BackWin.getMessageCode();
            }

        } else {
            // 判断是不是将军

             /* boolean jiangJun = Rule.jiangJun(cheses, cheseIndex.getCode()); if(jiangJun)
			  { gameState = GameMessage.JiangJun.getMessageCode(); }*/

        }

        // 数据存入redis
        cheses[codeIndex.getStartX()][codeIndex.getStartY()] = 0;
        cheses[codeIndex.getEndX()][codeIndex.getEndY()] = code;
        redisTemplate.opsForValue().set(Constant.CHECKERBOARD_REDIS_ID + checkerboardinfo.getCheckerBoardID(), cheses);
        return "200:" + gameState;
    }

    // 发送消息
    @SuppressWarnings("unused")
    private void sendMessage(WebSocketSession session, TextMessage message) {
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(message);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
