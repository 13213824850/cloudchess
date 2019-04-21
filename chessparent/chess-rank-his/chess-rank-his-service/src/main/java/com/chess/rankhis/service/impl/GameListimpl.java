package com.chess.rankhis.service.impl;

import com.chess.common.constant.MQConstant;
import com.chess.common.enumcodes.GameMessage;
import com.chess.common.util.Msg;
import com.chess.common.vo.CheseIndex;
import com.chess.rankhis.client.UserClient;
import com.chess.rankhis.enty.GameList;
import com.chess.rankhis.mapper.GameListMapper;
import com.chess.rankhis.service.GameListService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class GameListimpl implements GameListService {

    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    private GameListMapper gameListMapper;

    @Autowired
    UserClient userClient;

    @Override
    public Msg getGameLists(Integer type) {
        Example example = new Example(GameList.class);
        Example.Criteria criteria = example.createCriteria();
        if(type != 0){
            criteria.andEqualTo("matchType", type);
        }
        List<GameList> gameLists = gameListMapper.selectByExample(example);
        return Msg.success().add("gameLists",gameLists);
    }

    @Override
    public Msg addGameList(GameList gameList) {
        gameList.setRedNickName(userClient.getUserInfoByName(gameList.getRedNickName()).getNickName());
        gameList.setBlackNickName(userClient.getUserInfoByName(gameList.getBlackNickName()).getNickName());
        gameListMapper.insert(gameList);
        //发送消息tianjia对局a
        CheseIndex cheseIndex = new CheseIndex();
        cheseIndex.setMessageCode(GameMessage.ADD_GAME_LIST.getMessageCode());
        cheseIndex.add("gameList",gameList);
        amqpTemplate.convertAndSend(MQConstant.CHESEINDEX_EXCHANGE,MQConstant.CHESEINDEX_KEY,cheseIndex);
        return Msg.success();
    }

    @Override
    public Msg deleteGameList(String checkBoardInfoId) {
        Example example = new Example(GameList.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("checkBoardInfoId", checkBoardInfoId);
        gameListMapper.deleteByExample(example);
        //删除
        CheseIndex cheseIndex = new CheseIndex();
        cheseIndex.setMessageCode(GameMessage.DELETE_GAME_LIST.getMessageCode());
        cheseIndex.add("checkBoardInfoId",checkBoardInfoId);
        amqpTemplate.convertAndSend(MQConstant.CHESEINDEX_EXCHANGE,MQConstant.CHESEINDEX_KEY,cheseIndex);
        return Msg.success();
    }
}
