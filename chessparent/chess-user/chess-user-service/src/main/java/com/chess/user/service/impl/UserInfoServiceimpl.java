package com.chess.user.service.impl;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Service;

import com.chess.common.enumcodes.ExceptionEnum;
import com.chess.common.exception.ChessException;
import com.chess.common.util.Msg;
import com.chess.common.vo.ActionCode;
import com.chess.user.mapper.UserInfoMapper;
import com.chess.user.pojo.UserInfo;
import com.chess.user.service.UserInfoService;
import com.chess.user.utils.CodecUtils;
import com.chess.user.vo.UserLogin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import sun.security.util.Password;

@Service
@Slf4j
public class UserInfoServiceimpl implements UserInfoService {

	@Autowired
	private AmqpTemplate amqpTemplate;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private RedisTemplate redisTemplate;
	private final String retry_send_mail = "retry:send:mail:";

	/*
	 * @Autowired private RankService rankService;
	 * 
	 * @Autowired private UserInfoMapper userInfoMapper;
	 */
	@Override
	public Msg addUser(UserInfo userInfo) {
		// 查询用户是否存在
		UserInfo user = getUserInfo(userInfo.getUserName());
		if (user != null && user.getCode().length() < 5) {
			throw new ChessException(ExceptionEnum.ALERYEXIT);
		}
		// 判断用户是否已注册但未激活
		if (user != null && user.getCode() != null && user.getCode().length() >= 5) {
			// 判断两次邮件发送间隔是否超过指定时间
			String key = retry_send_mail + user.getUserName();
			Object object = redisTemplate.opsForValue().get(key);
			if (object == null) {
				// 再次发送激活邮件
				ActionCode ac = new ActionCode(user.getCode(),user.getUserName());
				try {
					amqpTemplate.convertAndSend("chess.email.exchange","email.verify.code",ac);
					//设置发送间隔时间
					redisTemplate.opsForValue().set(key, 1, 10, TimeUnit.MINUTES);
					return Msg.success();
				}catch(Exception e){
					log.warn("发送账号激活消息失败",user.getUserName());
				}
			} else {
				throw new ChessException(ExceptionEnum.NOAGAINSEND);
			}
		}
		String password = userInfo.getPassword();
		password = CodecUtils.md5Hex(password, userInfo.getUserName().substring(0, userInfo.getUserName().length() - 3));
		String uuid = UUID.randomUUID().toString();
		userInfo.setPassword(password);
		userInfo.setCode(uuid);
		userInfo.setCreateTime(new Date());
		userInfo.setUpdateTime(new Date());
		userInfo.setAge(0);
		byte sex = 0;
		userInfo.setSex(sex);
		userInfoMapper.insert(userInfo);
		ActionCode ac = new ActionCode(uuid, userInfo.getUserName());
		amqpTemplate.convertAndSend("chess.email.exchange","email.verify.code",ac);
		return Msg.success();
	}

	//根据用户密码查询
	@Override
	public UserInfo queryUserInfo( String userName, String password ) {
		log.info("userlogin{}",userName);
		UserInfo userInfo = getUserInfo(userName);
		if(userInfo != null ){
			 password = CodecUtils.md5Hex(password, userName.substring(0, userInfo.getUserName().length() - 3));
			if(userInfo.getPassword().equals(password)){
				return userInfo;
			}
		}
		return null;
	}

	// 查询用户
	@Override
	public UserInfo getUserInfo(String userName) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName(userName);
		userInfo = userInfoMapper.selectOne(userInfo);
		UserInfo userInfo1 = userInfoMapper.selectOne(userInfo);
		log.info("userinfo{}/n{}",userInfo,userInfo1);
		return userInfo;
	}

	// 激活账号
	@Override
	public Msg activeAccount(ActionCode acCode) {
		String userName = acCode.getUserName();
		UserInfo user = getUserInfo(userName);
		if (user == null ) {
			throw new ChessException(ExceptionEnum.INVALIDLINE);
		}
		if(!user.getCode().equals(acCode.getCode())) {
			throw new ChessException(ExceptionEnum.INVALIDLINE);
		}
		user.setCode("0");
		userInfoMapper.updateByPrimaryKey(user);
		return Msg.success();
	}


}
