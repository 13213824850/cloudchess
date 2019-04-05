package com.chess.rankhis.api;

import com.chess.common.util.Msg;
import com.chess.rankhis.enty.GameRecord;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

public interface HistoryApi {
    //添加对局记录
    @PostMapping("addRecord")
    public Msg addGameRecord(@Validated GameRecord gameRecord);

    //查询对局记录
    @GetMapping("getGameRecord/{pn}")
    public Msg getGameRecord(@RequestHeader("username")String userName, @PathVariable(value = "pn")Integer pn);
}
