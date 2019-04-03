package com.chess.user.service;

import com.chess.common.util.Msg;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 上午 10:03 2019/3/30 0030
 * @Modifide by:
 */
public interface FriendLaunchMessageService {
    Msg addFriendLaunchMessage(String userName, String friendUserName);

    Msg updateMessage(String userName, Integer messageId);
}
