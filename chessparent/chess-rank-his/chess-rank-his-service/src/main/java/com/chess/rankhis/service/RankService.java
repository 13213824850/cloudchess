package com.chess.rankhis.service;

import com.chess.rankhis.enty.Rank;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 4:25 2019/3/30 0030
 * @Modifide by:
 */
public interface RankService {
    Double getRankGrade(String userName);
    void updateRank(String userName,Boolean result);
    Rank getRank(String userName );
    void updateRankPlayCount(String userName, Boolean result);
}
