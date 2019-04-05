package com.chess.rankhis.web;

import com.chess.common.util.Msg;
import com.chess.rankhis.enty.GameRecord;
import com.chess.rankhis.service.GameRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@Api(value = "测试对局记录")
public class GameRecordController {

    @Autowired
    private GameRecordService gameRecordService;

    //添加对局记录
    @PostMapping("addRecord")
    @ApiOperation(value = "添加記錄")
    public Msg addGameRecord(@Validated GameRecord gameRecord, String userName){

        return gameRecordService.addGameRecord(gameRecord,userName);
    }
    //查询对局记录
    @ApiOperation(value = "查詢記錄")
    @GetMapping("getGameRecord/{pn}")
    public Msg getGameRecord(@RequestHeader("username")String userName, @PathVariable(value = "pn")Integer pn){
        return gameRecordService.getGameRecord(userName,pn);
    }
}
