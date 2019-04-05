package com.chess.rankhis.service.impl;

import com.chess.common.enumcodes.GameMessage;
import com.chess.common.util.Msg;
import com.chess.rankhis.enty.GameRecord;
import com.chess.rankhis.mapper.GameRecordMapper;
import com.chess.rankhis.service.GameRecordService;
import com.chess.rankhis.service.RankService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Transactional
@Service
public class GameRecordSeviceimpl implements GameRecordService {
    @Autowired
    private GameRecordMapper gameRecordMapper;
    @Autowired
    private RankService rankService;
    @Value("${history.showcount}")
    private static int pageSize;
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
    public Msg addGameRecord(GameRecord gameRecord,String winUserName) {
        String userName = gameRecord.getUserName();
        String otherName = gameRecord.getOtherUserName();
        String failUserName = userName==winUserName ? otherName : userName;
        gameRecord.setUserName(winUserName);
        gameRecord.setOtherUserName(failUserName);
        gameRecord.setResult(true);
        gameRecordMapper.insert(gameRecord);
        GameRecord gameRecordnext = new GameRecord();
        gameRecordnext.setUserName(failUserName);
        gameRecordnext.setOtherUserName(winUserName);
        gameRecordnext.setResult(false);
        gameRecordnext.setCreated(gameRecord.getCreated());
        gameRecordnext.setUpdated(gameRecord.getUpdated());
        gameRecordnext.setPlayTime(gameRecord.getPlayTime());
        gameRecordnext.setType(gameRecord.getType());
        gameRecordMapper.insert(gameRecordnext);
        if(gameRecord.getType() == GameMessage.RankGame.getMessageCode()){
            //更改rank
            rankService.updateRank(winUserName,gameRecord.getResult());
            rankService.updateRank(failUserName,gameRecordnext.getResult());
        }
        return Msg.success();
    }
}
