package com.chess.rankhis.service.impl;

import com.chess.common.enumcodes.GameMessage;
import com.chess.common.util.Msg;
import com.chess.rankhis.client.UserClient;
import com.chess.rankhis.enty.GameRecord;
import com.chess.rankhis.mapper.GameRecordMapper;
import com.chess.rankhis.service.GameRecordService;
import com.chess.rankhis.service.RankService;
import com.chess.user.pojo.UserInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class GameRecordSeviceimpl implements GameRecordService {
    @Autowired
    private GameRecordMapper gameRecordMapper;
    @Autowired
    private RankService rankService;
    @Value("${history.showcount}")
    private static int pageSize;

    @Autowired
    UserClient userClient;
    @Override
    public Msg getGameRecord(String userName, Integer pn) {
        PageHelper.startPage(pageSize*pn,pageSize);
        Example example = new Example(GameRecord.class);
        example.setOrderByClause("updated DESC");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userName",userName);
        List<GameRecord> gameRecords = gameRecordMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo<>(gameRecords);
        return Msg.success().add("pageInfo",pageInfo);
    }

    @Override
    public Msg addGameRecord(GameRecord gameRecord) {
        Date date = new Date();
        String userName = gameRecord.getUserName();
        String otherName = gameRecord.getOtherUserName();
        UserInfo userInfo  = userClient.getUserInfoByName(userName);
        UserInfo otherUserInfo = userClient.getUserInfoByName(otherName);
        gameRecord.setUserName(userName);
        gameRecord.setOtherUserName(otherName);
        gameRecord.setOtherNickName(otherUserInfo.getNickName());
        gameRecord.setResult(true);
        gameRecord.setPlayTime(gameRecord.getPlayTime());
        gameRecord.setCreated(date);
        gameRecord.setUpdated(date);
        gameRecord.setType(gameRecord.getType());
        gameRecordMapper.insert(gameRecord);
        GameRecord gameRecordnext = new GameRecord();
        gameRecordnext.setUserName(otherName);
        gameRecordnext.setOtherUserName(userName);
        gameRecordnext.setResult(false);
        gameRecordnext.setCreated(date);
        gameRecordnext.setUpdated(date);
        gameRecordnext.setOtherNickName(userInfo.getNickName());
        gameRecordnext.setPlayTime(gameRecord.getPlayTime());
        gameRecordnext.setType(gameRecord.getType());
        gameRecordMapper.insert(gameRecordnext);

        if(gameRecord.getType() == GameMessage.RankGame.getMessageCode()){
            //更改rank
            rankService.updateRank(userName,gameRecord.getResult());
            rankService.updateRank(otherName,gameRecordnext.getResult());
        }else{
            //更新对局数
            rankService.updateRankPlayCount(userName,gameRecord.getResult());
            rankService.updateRankPlayCount(otherName,gameRecordnext.getResult());
        }
        return Msg.success();
    }
}
