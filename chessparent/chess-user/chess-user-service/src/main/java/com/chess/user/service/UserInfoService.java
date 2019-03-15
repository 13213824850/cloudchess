package com.chess.user.service;

import javax.servlet.http.HttpServletResponse;

import com.chess.common.util.Msg;
import com.chess.common.vo.ActionCode;
import com.chess.user.pojo.UserInfo;
import com.chess.user.vo.UserLogin;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserInfoService {
	Msg addUser(UserInfo userInfo);

	UserInfo queryUserInfo(String userName, String password);
	
	UserInfo getUserInfo(String userName);
	Msg activeAccount(ActionCode acCode);
}
