package com.chess.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.chess.common.util.Msg;
import com.chess.common.vo.ActionCode;
import com.chess.user.pojo.UserInfo;
import com.chess.user.service.UserInfoService;
import com.chess.user.vo.UserLogin;

import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserInfoControllelr {

	@Autowired
	private UserInfoService userInfoService;
	
	@ApiOperation(value="添加用户",notes="")
	@PostMapping("/register")
	public Msg addUser(@ModelAttribute @RequestBody @Validated UserInfo userInfo) {
		return userInfoService.addUser(userInfo);
	}
	
	@ApiOperation(value="激活用户",notes="邮箱链接")
	@GetMapping("/activeAcount")
	public Msg activeAccount(@ModelAttribute @RequestBody @Validated ActionCode ac) {
		return userInfoService.activeAccount(ac);
	}

	/**
	 * @Auther:huang yuan li
	 * @Description:查询用户根据密码
	 * @date: Create in ${TIME} ${DATE}
	 */
	@PostMapping(value = "/query")
	public UserInfo queryUserInfo( @RequestParam("userName")String userName,@RequestParam("password")
								  String password){
		return userInfoService.queryUserInfo(userName, password);
	}
	@GetMapping("/test")
	public String test() {
		return "test";
	}

	//查询个人信息
	@GetMapping("getUserInfo")
	public Msg getUserpro(@RequestHeader("username")String userName){
		return userInfoService.getUserpro(userName);
	}
/*	@GetMapping("/getUserInfo/{userName}")
	public Msg getUserById(@PathVariable("userName") String userName) {
		return userInfoService.getUserByUserName(userName);
	}
	@GetMapping("/getUser/{cookie}")
	public Msg getUser(@PathVariable("cookie") String cookie) {
		return userInfoService.getUser(cookie);
	}*/
	
}
