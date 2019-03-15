package com.chess.user.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class UserLogin implements Serializable{
	@Email(message = "邮箱格式不正确")
	@NotBlank(message = "用户不能为空")
	private	String userName;
	@NotBlank(message = "密码不能为空")
	@Length(min = 6, max = 11, message = "密码长度在6-11位" )
	private String password;
}
