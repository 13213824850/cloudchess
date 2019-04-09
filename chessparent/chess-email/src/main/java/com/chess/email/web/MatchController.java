package com.chess.email.web;

import com.chess.common.util.Msg;
import com.chess.email.matchplay.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: huang yuan li
 * @Description:匹配对局确认
 * @date: Create in 下午 3:56 2019/4/2 0002
 * @Modifide by:
 */
@RestController
public class MatchController {

    @Autowired
    private MatchService matchService;
    @GetMapping("confirmMatch")
    public Msg confirmMatch(@RequestHeader("username")String userName){
        return matchService.confirmMatch(userName);
    }
    @GetMapping("refuseGame")
    public Msg refuseGame(@RequestHeader("username")String userName){
        return matchService.refuseGame(userName);
    }

    //发起对战
    @GetMapping(value = "lauchPlay/{oppUserName}")
    public Msg lauchPlay(@RequestHeader("username")String userName, @PathVariable("oppUserName")String oppUserName){
        return matchService.lauchPlay(userName, oppUserName);
    }
    @GetMapping(value = "agreePlay")
    public Msg agreePlay(@RequestHeader("username")String userName){
        return null;
    }

}
