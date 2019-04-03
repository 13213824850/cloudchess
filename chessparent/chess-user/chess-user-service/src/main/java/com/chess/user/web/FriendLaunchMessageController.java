package com.chess.user.web;

import com.chess.common.util.Msg;
import com.chess.user.service.FriendLaunchMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 上午 10:04 2019/3/30 0030
 * @Modifide by:
 */
@RestController
public class FriendLaunchMessageController {
    @Autowired
    private FriendLaunchMessageService fls;

    @PostMapping("addMessage")
    public Msg addFriendLaunchMessage(String friendUserName, @RequestHeader("username") String userName){
            return fls.addFriendLaunchMessage(userName,friendUserName);
    }

    @GetMapping("updateMessage/{id}")
    public Msg updateMessage(@RequestHeader("username")String userName, @PathVariable("id") Integer messageId){
        return fls.updateMessage(userName,messageId);
    }
}
