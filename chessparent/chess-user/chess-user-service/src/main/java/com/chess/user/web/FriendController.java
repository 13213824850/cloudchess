package com.chess.user.web;

import com.chess.common.util.Msg;
import com.chess.user.pojo.Friend;
import com.chess.user.service.FriendService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 上午 9:56 2019/3/30 0030
 * @Modifide by:
 */
@RestController
public class FriendController {
    @Autowired
    private FriendService friendService;

    @GetMapping("getFriends")
    @ApiOperation(value = "获取用户的好友列表",notes = "必须已登录")
    public Msg getFriends(@RequestHeader("username") String userName){
        return friendService.getFriends(userName);
    }

    @GetMapping("updateFriendLine/{onLine}")
    @ApiOperation(value = "更新好友列表用户在线信息")
    public Msg updateFriendLine(@RequestHeader("username") String userName, @PathVariable("onLine") Integer onLine){
        return friendService.updateFriendLine(userName, onLine);
    }
    @GetMapping("getFriend/{onLine}")
    public List<Friend> getFriendsByLine(@RequestHeader("username")String userName, @PathVariable("onLine") Integer onLine){
        return friendService.getFriendsByLine(userName, onLine);
    }
}
