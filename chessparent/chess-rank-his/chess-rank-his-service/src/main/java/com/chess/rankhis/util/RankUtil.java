package com.chess.rankhis.util;

import com.chess.rankhis.enty.Rank;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RankUtil {

    /* 获取rank分值

     */
    public Double getRankGrad(Rank rank) {
        int rankGrade = rank.getRankGrade();
        int rankState = rank.getRankGradeStage();
        int star = rank.getStar();
        int winCount = rank.getWinCount();
        int transportCount = rank.getTransportCount();
        int continusWin = rank.getContinusWin();
        int continusTransport = rank.getContinusTransport();
        int starCount = rankGrade * 9 + (3 - rankState) * 3 + star + continusWin - continusTransport;
        int playCount = winCount + transportCount;
        double score = starCount * 10;
        if (playCount != 0) {
            score = score * (winCount / playCount);
        }
        return score;
    }
    //更改rank
    public Rank updateRank(Rank rank, boolean result){
        //赢了
        int star = rank.getStar();
        if (result){
            if (star < 3){
                rank.setStar(star + 1);
            } else if (rank.getRankGradeStage() > 1){
                rank.setStar(1);
                rank.setRankGradeStage(rank.getRankGradeStage() - 1);
            } else if (rank.getRankGrade() < 7){
                rank.setStar(1);
                rank.setRankGradeStage(3);
                rank.setRankGrade(rank.getRankGrade() + 1);
            } else {
                rank.setStar(star + 1);
            }
            rank.setWinCount(rank.getWinCount() + 1);
            rank.setContinusWin(rank.getContinusWin() + 1);
            rank.setTransportCount(0);
        } else {
          if (star > 0){
              rank.setStar(star - 1);
          }else if (rank.getRankGradeStage() < 3){
              rank.setStar(3);
              rank.setRankGradeStage(rank.getRankGradeStage() + 1);
          }else if (rank.getRankGrade() > 1){
              rank.setStar(3);
              rank.setRankGradeStage(3);
              rank.setRankGrade(rank.getRankGrade() -  1);
          }
          rank.setTransportCount(rank.getTransportCount() + 1);
          rank.setContinusWin(0);
          rank.setContinusTransport(rank.getContinusTransport() + 1);
        }
        rank.setUpdated(new Date());
        return rank;
    }
}
