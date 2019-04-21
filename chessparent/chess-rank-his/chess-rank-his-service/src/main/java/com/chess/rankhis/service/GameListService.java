package com.chess.rankhis.service;

import com.chess.common.util.Msg;
import com.chess.rankhis.enty.GameList;

public interface GameListService {
    Msg getGameLists(Integer type);

    Msg addGameList(GameList gameList);

    Msg deleteGameList(String checkBoardInfoId);
}
