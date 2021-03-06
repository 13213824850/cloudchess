package com.chess.rankhis.service.impl;

import com.chess.rankhis.client.UserClient;
import com.chess.rankhis.enty.Rank;
import com.chess.rankhis.mapper.RankMapper;
import com.chess.rankhis.service.RankService;
import com.chess.rankhis.util.RankUtil;
import com.chess.user.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class RankServiceimpl implements RankService {
    @Autowired
    private RankMapper rankMapper;
    @Autowired
    private RankUtil rankUtil;
    @Autowired
    private UserClient userClient;

    @Override
    public Double getRankGrade(String userName) {
        Rank rank = getRank(userName);
        Double rankGrad = rankUtil.getRankGrad(rank);
        return rankGrad;
    }

    @Override
    public void updateRank(String userName, Boolean result) {
        Rank rank = getRank(userName);
        rank = rankUtil.updateRank(rank, result);
        rankMapper.updateByPrimaryKeySelective(rank);
    }

    @Override
    public Rank getRank(String userName) {
        Rank rank = new Rank();
        rank.setUserName(userName);
        Rank rankInfo = rankMapper.selectOne(rank);
        if (rankInfo == null) {
            //添加rank性息
            addRank(rank, userName);
            rankInfo = rank;
        }
        return rankInfo;
    }

    @Override
    public void updateRankPlayCount(String userName, Boolean result) {
        Rank rank = getRank(userName);
        if (result) {
            rank.setWinCount(rank.getWinCount() + 1);
        } else {
            rank.setTransportCount(rank.getTransportCount() + 1);
        }
        rankMapper.updateByPrimaryKey(rank);
    }


    //初始化Rank
    public void addRank(Rank rank,String userName) {
        //查询昵称
        UserInfo userInfoByName = userClient.getUserInfoByName(userName);
        rank.setUserName(userName);
        rank.setNickName(userInfoByName.getNickName());
        rank.setCreated(new Date());
        rank.setUpdated(new Date());
        rank.setContinusTransport(0);
        rank.setContinusWin(0);
        rank.setWinCount(0);
        rank.setTransportCount(0);
        rank.setProtectCount(0);
        rank.setRankGradeStage(3);
        rank.setRankGrade(1);
        rank.setStar(0);
        rankMapper.insert(rank);
    }
}
