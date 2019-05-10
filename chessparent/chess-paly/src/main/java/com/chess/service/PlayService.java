package com.chess.service;

import com.alibaba.fastjson.JSON;
import com.chess.client.FriendClient;
import com.chess.client.GameListClient;
import com.chess.client.HistoryClient;
import com.chess.client.RankClient;
import com.chess.common.cheserules.Rule;
import com.chess.common.constant.Constant;
import com.chess.common.constant.MQConstant;
import com.chess.common.enumcodes.GameMessage;
import com.chess.common.util.CheseCode;
import com.chess.common.util.RuleUtil;
import com.chess.common.vo.CheckerBoardInfo;
import com.chess.common.vo.CheseIndex;
import com.chess.common.vo.CodeIndex;
import com.chess.play.WsHandler;
import com.chess.rankhis.enty.GameRecord;
import com.chess.user.pojo.Friend;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 1:02 2019/3/30 0030
 * @Modifide by:
 */
@Service
@Slf4j
public class PlayService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private HistoryClient historyClient;
    @Autowired
    private RankClient rankClient;
    @Autowired
    private FriendClient friendClient;
    @Autowired
    private GameListClient gameListClient;

    /**
     * @param checkerBoardInfo 棋盘信息
     * @param session          当前session userName 当前用户名
     * @Auther:huang yuan li
     * @Description:断线重连
     * @date: Create in ${TIME} ${DATE}
     */
    public void againConnectMatch(CheckerBoardInfo checkerBoardInfo, WebSocketSession session, String userName) {
        //获取棋盘信息
        int[][] cheses = (int[][]) redisTemplate.opsForValue()
                .get(Constant.CHECKERBOARD_REDIS_ID + checkerBoardInfo.getCheckerBoardID());

        CheseIndex cheseIndex = new CheseIndex();
        cheseIndex.setMessageCode(GameMessage.InitGame.getMessageCode());
        String turnMe = (String) redisTemplate.opsForValue()
                .get("turnMe:" + checkerBoardInfo.getCheckerBoardID());
        cheseIndex.add("checkerBoardInfo", checkerBoardInfo).add("cheses", cheses).add("startTime", checkerBoardInfo.getDate());
        cheseIndex.setOppUserName(checkerBoardInfo.getOppUserName());
        cheseIndex.setTurnMe(turnMe);
        cheseIndex.setGameState(checkerBoardInfo.getGameState());
        cheseIndex.setRedUserName(checkerBoardInfo.getCode() == CheseCode.Red.getCode() ?
                userName : checkerBoardInfo.getOppUserName());
        Instant instant = (Instant) redisTemplate.opsForValue().get(Constant.SINGLE_OVER_TIME + userName);
        //获取剩余时间
        int time = 60;
        if (instant != null) {
            time = (int) (instant.getEpochSecond() - Instant.now().getEpochSecond());
        }
        cheseIndex.setRamainTime(time);
        sendMessage(session, cheseIndex);
        //设置用户状态
        redisTemplate.opsForValue().set(Constant.KEEP_ALIVE + userName, 2);
        log.info("建立连接 开始对局棋盘{}", JSON.toJSONString(cheseIndex));
    }


    public void cheseMove(WebSocketSession session, CheseIndex cheseIndex) throws IOException {

        String userName = WsHandler.sessionIds.get(session.getId());
        //检查是否超时
        Instant time = (Instant) redisTemplate.opsForValue().get(Constant.SINGLE_OVER_TIME + userName);
        if (time == null) {
            return;
        }
        // 获取棋盘
        CheckerBoardInfo checkerboardinfo = (CheckerBoardInfo) redisTemplate.opsForValue()
                .get(Constant.CHECKBOARD_INFO + userName);
        TextMessage sendMsg = null;
        // 检查游戏状态如果游戏为结束则结束
        if (checkerboardinfo.getGameState() == GameMessage.RedWin.getMessageCode()
                || checkerboardinfo.getGameState() == GameMessage.BackWin.getMessageCode()) {
            cheseIndex.setMessage("游戏结束");
            cheseIndex.setGameState(checkerboardinfo.getGameState());
            cheseIndex.setMessageCode(checkerboardinfo.getGameState());
            sendMessage(session, cheseIndex);
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
            String turnMe = WsHandler.sessionIds.get(session.getId());
            turnMe = (String) redisTemplate.opsForValue().get("turnMe:" + checkerboardinfo.getCheckerBoardID());
            cheseIndex.setMessageCode(GameMessage.CheseMoveErr.getMessageCode());
            cheseIndex.setMessage(GameMessage.CheseMoveErr.getMessage());
            cheseIndex.setTurnMe(turnMe);
            sendMessage(session, cheseIndex);
        }

    }

    private void gameResullt(CheseIndex cheseIndex, CheckerBoardInfo checkerboardinfo, int gameState,
                             WebSocketSession session) {
        String userName = WsHandler.sessionIds.get(session.getId());
        // 更改turnMe
        String turnMe = checkerboardinfo.getOppUserName();
        if (gameState == GameMessage.BackWin.getMessageCode() || gameState == GameMessage.RedWin.getMessageCode()) {
            log.debug("游戏结束gameState{}", gameState);
            // 清楚数据
            redisTemplate.delete(checkerboardinfo.getCheckerBoardID());
            cheseIndex.setGameState(gameState);
            checkerboardinfo.setGameState(gameState);
            redisTemplate.delete(Constant.CHECKBOARD_INFO + userName);
            redisTemplate.delete(Constant.CHECKBOARD_INFO + checkerboardinfo.getOppUserName());
            // 棋盘信息
            redisTemplate.delete("turnMe:" + checkerboardinfo.getCheckerBoardID());
            redisTemplate.delete(Constant.CHECKERBOARD_REDIS_ID + checkerboardinfo.getCheckerBoardID());
            //添加记录
            GameRecord gameRecord = new GameRecord();
            gameRecord.setType(checkerboardinfo.getType());
            gameRecord.setUserName(userName);
            gameRecord.setOtherUserName(checkerboardinfo.getOppUserName());
            gameRecord.setResult(true);
            int playTime = new Date().getMinutes() - checkerboardinfo.getDate().getMinutes();
            gameRecord.setPlayTime(playTime + "分钟");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    historyClient.addGameRecord(gameRecord);
                    //删除对战记录
                    gameListClient.deleteGameList(checkerboardinfo.getCheckerBoardID());
                    //更改状态
                }
            }).start();
            updateFriendShow(userName, Constant.LINE_ON);
            updateFriendShow(checkerboardinfo.getOppUserName(), Constant.LINE_ON);
        } else {
            cheseIndex.setGameState(GameMessage.PlayIng.getMessageCode());
            redisTemplate.opsForValue().set("turnMe:" + checkerboardinfo.getCheckerBoardID(), turnMe);
        }

        cheseIndex.setTurnMe(turnMe);
        //s删除超时信息
        redisTemplate.delete(Constant.SINGLE_OVER_TIME + userName);
        redisTemplate.opsForValue().set(Constant.SINGLE_OVER_TIME + turnMe, Instant.now().plusSeconds(60));
        cheseIndex.setRamainTime(60);
        // 向对方发送己方所走棋位
        // 向对方发送数据
        log.debug("对方userName{}", checkerboardinfo.getOppUserName());
        amqpTemplate.convertAndSend("chess.play.exchange", "play.message", cheseIndex);
        sendMessage(session, cheseIndex);
        if(redisTemplate.boundSetOps(checkerboardinfo.getCheckerBoardID()) != null){
            cheseIndex.setMessageCode(GameMessage.WATCH_PLAY_MOVE.getMessageCode());
            cheseIndex.setCheckBoardInfoId(checkerboardinfo.getCheckerBoardID());
            amqpTemplate.convertAndSend(MQConstant.CHESEINDEX_EXCHANGE, MQConstant.CHESEINDEX_KEY, cheseIndex);
        }

    }

    //检查棋子移动规则
    private String checkRule(CheckerBoardInfo checkerboardinfo, CheseIndex cheseIndex) {
        CodeIndex codeIndex = cheseIndex.getCodeIndex();
        if (!RuleUtil.checkOutBoard(codeIndex)) {
            log.info("请在棋盘内下棋");
            return "请在棋盘内下棋";
        }
        int code = codeIndex.getCode();
        if (!((checkerboardinfo.getCode() == CheseCode.Red.getCode() && code > 0)
                || (checkerboardinfo.getCode() == CheseCode.Back.getCode() && code < 0))) {
            log.info("不符合");
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

    /**
     * @Auther:huang yuan li
     * @Description:发送消息
     * @date: Create in ${TIME} ${DATE}
     */
    public void sendMessage(WebSocketSession session, Object object) {
        String jsonString = JSON.toJSONString(object);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(jsonString));
            } catch (IOException e) {
                e.printStackTrace();
                log.error("发送失败");
            }
        }
    }

    //获取rank分值
    public double getRankScore(String userName) {
        return rankClient.getRankGrade(userName);
    }

    //更新用户在线信息和好友列表
    public void updateFriendShow(String userName, int onLine) {
        redisTemplate.opsForValue().set(Constant.KEEP_ALIVE + userName, onLine);
        //更新信息
        friendClient.updateFriendLine(userName, onLine);
        //获取在线好友
        List<Friend> friendsByLine = friendClient.getFriendsByLine(userName, Constant.LINE_ON);
        CheseIndex cheseIndex = new CheseIndex();
        cheseIndex.setMessageCode(GameMessage.UPDATE_SHOW_FRIENDS.getMessageCode());
        cheseIndex.setMessage(GameMessage.UPDATE_SHOW_FRIENDS.getMessage());
        List<String> friendNames = friendsByLine.stream().map(f -> f.getFriendName()).collect(Collectors.toList());
        cheseIndex.add("friends", friendNames);
        cheseIndex.setUserName(userName);
        cheseIndex.add("onLine", onLine);
        amqpTemplate.convertAndSend(MQConstant.CHESEINDEX_EXCHANGE, MQConstant.CHESEINDEX_KEY, cheseIndex);
    }

    //向大厅所有人发送消息sendMessageToAll
    public void sendMessageToAll(CheseIndex cheseIndex, String userName) {
        String message = cheseIndex.getMessage();
        if (message.length() >= 100 || message.trim().length() == 0) {
            return;
        }
        sendMessage(WsHandler.sessionMap.get(userName), cheseIndex);
        Map<String, WebSocketSession> sessionMap = WsHandler.sessionMap;
        for (String str : sessionMap.keySet()) {
            if (str.equals(userName)) {
                continue;
            }
            Object o = redisTemplate.opsForValue().get(Constant.KEEP_ALIVE + str);
            if (o == null) {
                continue;
            }
            int state = (int) o;
            //匹配中或者在线则发送消息
            if (state == Constant.LINE_ON || state == Constant.LINE_MATCH) {
                sendMessage(sessionMap.get(str), cheseIndex);
            }
        }
        //通过mq向其他服务器发送消息 暂未写
    }

    //向单个人发送消息
    public void sendMessageToSingle(CheseIndex cheseIndex, String userName) {
        if (cheseIndex.getMessage().length() > 100 || cheseIndex.getMessage().trim().length() == 0) {
            return;
        }
        //接收方
        String toUserName = cheseIndex.getOppUserName();
        cheseIndex.setUserName(userName);
        sendMessage(WsHandler.sessionMap.get(toUserName), cheseIndex);
        sendMessage(WsHandler.sessionMap.get(userName), cheseIndex);
    }

    /**
     * @param userName         用户账号
     * @param checkBoardInfoId 棋盘id
     * @Description: 用户观战： 1、查询企盼信息 2、添加到观战集合、3、发送消息
     */
    public void watchPlay(String userName, String checkBoardInfoId) {
        Object cheses = redisTemplate.opsForValue()
                .get(Constant.CHECKERBOARD_REDIS_ID + checkBoardInfoId);
        CheseIndex cheseIndex = new CheseIndex();
        if (cheses == null) {
            cheseIndex.setMessageCode(GameMessage.WATCH_NO_FIND.getMessageCode());
            cheseIndex.setMessage(GameMessage.WATCH_NO_FIND.getMessage());
            sendMessage(WsHandler.sessionMap.get(userName), cheseIndex);
            return;
        }
        //添加观战集合
        redisTemplate.boundSetOps(checkBoardInfoId).add(userName);
        Object o = redisTemplate.opsForValue().get(checkBoardInfoId);
        if (o == null) {
            return;
        }
        String str = (String) o;
        String[] split = str.split(":");
        cheseIndex.setUserName(split[0]);
        cheseIndex.setOppUserName(split[1]);
        //强制类型转化
        int[][] chese = (int[][]) cheses;
        cheseIndex.add("cheses", chese);
        cheseIndex.setMessageCode(GameMessage.WATCH_PLAY.getMessageCode());
        sendMessage(WsHandler.sessionMap.get(userName), cheseIndex);
    }
}
