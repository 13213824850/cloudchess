package com.chess.rankhis.web;

import com.chess.common.util.Msg;
import com.chess.rankhis.service.RankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 4:25 2019/3/30 0030
 * @Modifide by:
 */
@RestController
@Api("rank相关")
public class RankController {
    @Autowired
    RankService rankService;

    @ApiOperation(value = "查詢rank分值")
    @GetMapping("getRankGrade")
    public Double getRankGrade(@RequestHeader("username")String userName){

        return rankService.getRankGrade(userName);
    }
    @ApiOperation(value = "查詢rank信息")
    @GetMapping("getRank/{userName}")
    public Msg getRank(@PathVariable("userName") String userName){
        return Msg.success().add("rank", rankService.getRank(userName));
    }
}
