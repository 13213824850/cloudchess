package com.chess.user.service.impl;

import com.chess.common.constant.Constant;
import com.chess.common.util.Msg;
import com.chess.user.mapper.FriendMapper;
import com.chess.user.pojo.Friend;
import com.chess.user.pojo.FriendLaunchMessage;
import com.chess.user.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
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
    @Resource
    private RedisTemplate redisTemplate;
    @Override
    public Msg getFriends(String userName) {
        Friend friend = new Friend();
        friend.setUserName(userName);
        List<Friend> friends = friendMapper.select(friend);
        return Msg.success().add("friends",friends);
    }

    @Override
    public Msg updateFriendLine(String userName, Integer onLine) {
        Example example = new Example(Friend.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("friendName",userName);
        Friend friend = new Friend();
        friend.setOnLine(onLine);
        friendMapper.updateByExampleSelective(friend,example);
        return Msg.success();
    }

    @Override
    public List<Friend> getFriendsByLine(String userName, Integer onLine) {
        Example example = new Example(Friend.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userName", userName);
        criteria.andEqualTo("onLine", onLine);
        List<Friend> friends = friendMapper.selectByExample(example);
        return friends;
    }

    @Override
    public void addFriend(FriendLaunchMessage friendLaunchMessage) {
        //获取用户状态
        Integer userState = (Integer) redisTemplate.opsForValue().get(Constant.KEEP_ALIVE + friendLaunchMessage.getUserName());
        Integer friendState = (Integer) redisTemplate.opsForValue().get(Constant.KEEP_ALIVE + friendLaunchMessage.getLaunchUserName());
        userState = userState == null ? 0 : userState;
        friendState = friendState == null ? 0: friendState;
        Date date = new Date();
        Friend friend = new Friend();
        friend.setCreated(date);
        friend.setUpdated(date);
        friend.setUserName(friendLaunchMessage.getUserName());
        friend.setFriendNickName(friendLaunchMessage.getLaunchNickName());
        friend.setFriendName(friendLaunchMessage.getLaunchUserName());
        friend.setFriendSex(friendLaunchMessage.getLaunchSex());
        friend.setOnLine(userState);

        Friend otherFriend = new Friend();
        otherFriend.setCreated(date);
        otherFriend.setUpdated(date);
        otherFriend.setUserName(friendLaunchMessage.getLaunchUserName());
        otherFriend.setFriendNickName(friendLaunchMessage.getNickName());
        otherFriend.setFriendName(friendLaunchMessage.getUserName());
        otherFriend.setFriendSex(friendLaunchMessage.getSex());
        otherFriend.setOnLine(friendState);
        friendMapper.insert(friend);
        friendMapper.insert(otherFriend);
    }
}
