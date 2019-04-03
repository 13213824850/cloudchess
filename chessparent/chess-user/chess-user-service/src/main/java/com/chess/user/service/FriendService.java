package com.chess.user.service;

import com.chess.common.util.Msg;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 上午 9:56 2019/3/30 0030
 * @Modifide by:
 */
public interface FriendService {
    Msg getFriends(String userName);

    Msg updateFriendLine(String userName);
}
