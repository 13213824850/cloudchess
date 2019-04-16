package com.chess.user.web;

import com.chess.common.util.Msg;
import com.chess.common.vo.ActionCode;
import com.chess.user.pojo.UserInfo;
import com.chess.user.service.UserInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
	@GetMapping("/getUserInfoByNickNAme/{nickName}")
	public Msg getUserByNickName(@PathVariable("nickName") String nickName) {
		return userInfoService.getUserByNickName(nickName);
	}

	
}
