package com.chess.rankhis.api;

import com.chess.common.util.Msg;
import com.chess.rankhis.enty.GameList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface GameListApi {
    //显示列表
    @GetMapping("getGameList/{pn}/{type}")
    public Msg getGameLists(@PathVariable("pn") Integer pn, @PathVariable("type")Integer type);

    @GetMapping("delete/{checkBoardInfoId}")
    public Msg deleteGameList(@PathVariable("checkBoardInfoId")String checkBoardInfoId);

    @PostMapping("addGameList")
    public Msg addGameList(@RequestBody GameList gameList);
}
