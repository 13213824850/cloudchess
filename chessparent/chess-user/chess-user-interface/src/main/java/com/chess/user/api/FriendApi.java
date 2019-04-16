package com.chess.user.api;

import com.chess.common.util.Msg;
import com.chess.user.pojo.Friend;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 上午 10:22 2019/3/30 0030
 * @Modifide by:
 */
public interface FriendApi {
    @GetMapping("getFriends")
    public Msg getFriends(@RequestHeader("username") String userName);

    @GetMapping("updateFriendLine/{onLine}")
    public Msg updateFriendLine(@RequestHeader("username") String userName, @PathVariable("onLine") Integer onLine);

    @GetMapping("getFriend/{onLine}")
    public List<Friend> getFriendsByLine(@RequestHeader("username") String userName, @PathVariable("onLine") Integer onLine);
}
