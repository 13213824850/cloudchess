package com.chess.rankhis.web;

import com.chess.common.util.Msg;
import com.chess.rankhis.enty.GameList;
import com.chess.rankhis.service.GameListService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameListController {

    @Autowired
    private GameListService gameListService;

    //显示列表
    @GetMapping("getGameList/{type}")
    public Msg getGameLists(@PathVariable("type")Integer type){
        return gameListService.getGameLists(type);
    }

    @GetMapping("delete/{checkBoardInfoId}")
    public Msg deleteGameList(@PathVariable("checkBoardInfoId")String checkBoardInfoId){
        return gameListService.deleteGameList(checkBoardInfoId);
    }

    @PostMapping("addGameList")
    public Msg addGameList(@RequestBody GameList gameList){
        return gameListService.addGameList(gameList);
    }
}
