package com.chess.user.service.impl;

import com.chess.common.util.Msg;
import com.chess.user.mapper.FriendMapper;
import com.chess.user.pojo.Friend;
import com.chess.user.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 上午 10:19 2019/3/30 0030
 * @Modifide by:
 */
@Service
public class FriendServiceimpl implements FriendService {
    @Autowired
    private FriendMapper friendMapper;
    @Override
    public Msg getFriends(String userName) {
        Friend friend = new Friend();
        friend.setUserName(userName);
        List<Friend> friends = friendMapper.select(friend);
        return Msg.success().add("friends",friends);
    }

    @Override
    public Msg updateFriendLine(String userName) {
        Example example = new Example(Friend.class);
        Friend friend = new Friend();
        friend.setFriendName(userName);
        friendMapper.updateByExampleSelective(friend,example);
        return Msg.success();
    }
}
