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

    @PostMapping("addMessage/{friendUserName}")
    public Msg addFriendLaunchMessage(@PathVariable("friendUserName") String friendUserName, @RequestHeader("username") String userName){
            return fls.addFriendLaunchMessage(userName,friendUserName);
    }

    /*
        @param state 1拒绝 2同意
        id 消息id
     */
    @GetMapping("updateMessage/{id}/{state}")
    public Msg updateMessage(@RequestHeader("username")String userName, @PathVariable("id") Integer messageId, @PathVariable("state")
                             Integer state){
        return fls.updateMessage(userName,messageId,state);
    }

    //查询是否有好友请求
    @GetMapping("geLaunchMessages/{state}")
    public Msg getLaunchMessageByState(@RequestHeader("username")String userName, @PathVariable("state") Integer state){
        return fls.getLaunchMessageByState(userName, state);
    }
    //查询请求个数
    @GetMapping("getLaunchCount")
    public Msg getLaunchCount(@RequestHeader("username")String userName){
        return fls.getLaunchCount(userName);
    }
}
