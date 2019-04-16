package com.chess.user.service.impl;

import com.chess.common.constant.Constant;
import com.chess.common.constant.MQConstant;
import com.chess.common.enumcodes.ExceptionEnum;
import com.chess.common.enumcodes.GameMessage;
import com.chess.common.exception.ChessException;
import com.chess.common.util.Msg;
import com.chess.common.vo.CheseIndex;
import com.chess.user.mapper.FriendLaunchMessageMapper;
import com.chess.user.pojo.FriendLaunchMessage;
import com.chess.user.pojo.UserInfo;
import com.chess.user.service.FriendLaunchMessageService;
import com.chess.user.service.FriendService;
import com.chess.user.service.UserInfoService;
import org.springframework.amqp.core.AmqpTemplate;
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
public class FrienndLaunchMessageServiceimpl implements FriendLaunchMessageService {
    @Autowired
    private FriendLaunchMessageMapper friendLaunchMessageMapper;
    @Autowired
    private FriendService friendService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Resource
    private RedisTemplate redisTemplate;
    @Override
    public Msg addFriendLaunchMessage(String userName, String friendUserName) {
        //获取用户信息
        UserInfo userInfo = userInfoService.getUserInfo(userName);
        UserInfo friendUserInfo = userInfoService.getUserInfo(friendUserName);
        if(friendUserInfo == null){
            throw new ChessException(ExceptionEnum.NOT_FIND_FRIEND);
        }
        //添加请求
        FriendLaunchMessage friendLaunchMessage = new FriendLaunchMessage();
        friendLaunchMessage.setCreated(new Date());
        friendLaunchMessage.setUpdated(new Date());
        friendLaunchMessage.setUserName(friendUserName);
        friendLaunchMessage.setLaunchUserName(userName);
        friendLaunchMessage.setLaunchNickName(userInfo.getNickName());
        friendLaunchMessage.setLaunchSex(userInfo.getSex());
        friendLaunchMessage.setNickName(friendUserInfo.getNickName());
        friendLaunchMessage.setSex(friendUserInfo.getSex());
        friendLaunchMessage.setState(0);
        friendLaunchMessageMapper.insert(friendLaunchMessage);

        //向对方发送消息
        int line = (int) redisTemplate.opsForValue().get(Constant.KEEP_ALIVE + friendUserName);
        if(line != 0){
            //如果在线发送消息
            CheseIndex cheseIndex = new CheseIndex();
            //设置发送给谁
            cheseIndex.setUserName(friendUserName);
            //设置状态吗
            cheseIndex.setMessageCode(GameMessage.RECEIVE_LAUNCH_MESSAGE.getMessageCode());
            amqpTemplate.convertAndSend(MQConstant.CHESEINDEX_EXCHANGE,MQConstant.CHESEINDEX_KEY,cheseIndex);
        }
        return Msg.success();
    }

    @Override
    public Msg updateMessage(String userName, Integer messageId, int state) {
        //获取消息
        FriendLaunchMessage friendLaunchMessage = friendLaunchMessageMapper.selectByPrimaryKey(messageId);
        if(!friendLaunchMessage.getUserName().equals(userName) || friendLaunchMessage.getState() != 0){
            return Msg.fail();
        }
        friendLaunchMessage.setState(state);
        if(state == 1){
            friendService.addFriend(friendLaunchMessage);
            friendLaunchMessageMapper.updateByPrimaryKey(friendLaunchMessage);
        }else if (state == 2){
            friendLaunchMessageMapper.updateByPrimaryKey(friendLaunchMessage);
        }
        //更改状态
        return Msg.success();
    }

    @Override
    public Msg getLaunchMessageByState(String userName, Integer state) {
        Example example = new Example(FriendLaunchMessage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userName", userName);
        criteria.andEqualTo("state", state);
        List<FriendLaunchMessage> friendLaunchMessages = friendLaunchMessageMapper.selectByExample(example);
        return Msg.success().add("friendLaunchMessages",friendLaunchMessages);
    }

    @Override
    public Msg getLaunchCount(String userName) {
        Example example = new Example(FriendLaunchMessage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("state",0);
        int count = friendLaunchMessageMapper.selectCountByExample(example);
        return Msg.success().add("count",count);
    }
}
