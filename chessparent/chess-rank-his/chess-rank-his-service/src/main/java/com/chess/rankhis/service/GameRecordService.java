package com.chess.rankhis.service;

import com.chess.common.util.Msg;
import com.chess.rankhis.enty.GameRecord;

public interface GameRecordService {
    Msg getGameRecord(String userName, Integer pn);
    Msg addGameRecord(GameRecord gameRecord,String winUserName);
}
