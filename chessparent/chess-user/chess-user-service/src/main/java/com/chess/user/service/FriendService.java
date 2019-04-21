package com.chess.user.service;

import com.chess.common.util.Msg;
import com.chess.user.pojo.Friend;
import com.chess.user.pojo.FriendLaunchMessage;

import java.util.List;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 上午 9:56 2019/3/30 0030
 * @Modifide by:
 */
public interface FriendService {
    Msg getFriends(String userName);

    Msg updateFriendLine(String userName, Integer onLine);

    List<Friend> getFriendsByLine(String userName, Integer onLine);

    void addFriend(FriendLaunchMessage friendLaunchMessage);

    Msg deleteFriend(String userName, String friendName);
}
