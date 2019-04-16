package com.chess.webtest;

import com.chess.client.HistoryClient;
import com.chess.common.util.Msg;
import com.chess.rankhis.enty.GameRecord;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {
    @Autowired
    private HistoryClient historyClient;
    @ApiOperation(value = "添加")
    @PostMapping(value = "add")
    public Msg add(@ModelAttribute GameRecord gameRecord){
        historyClient.addGameRecord(gameRecord);
        return null;
    }
    @ApiOperation(value = "chaxun")
    @GetMapping("get")
    public Msg get(){
        return null;
    }
}
