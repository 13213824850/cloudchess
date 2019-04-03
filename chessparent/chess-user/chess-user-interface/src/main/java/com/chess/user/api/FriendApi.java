package com.chess.user.api;

import com.chess.common.util.Msg;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 上午 10:22 2019/3/30 0030
 * @Modifide by:
 */
public interface FriendApi {
    @GetMapping("getFriends")
    public Msg getFriends(@RequestHeader("username")String userName);
    @GetMapping("updateFriendLine")
    public Msg updateFriendLine(@RequestHeader("username")String userName);
}
