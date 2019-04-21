package com.chess.user.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Table(name="user_info")
public class UserInfo implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

	@NotBlank(message = "用户不能为空")
	@Email(message = "邮箱格式不正确")
    private String userName;

	@NotBlank(message = "密码不能为空")
    //@JsonIgnore
    @Length(min = 6, max = 11, message = "密码长度在6-11位" )
    private String password;

    private Integer age;

    private Byte sex;

    @JsonIgnore
    private Integer platform;

    private String nickName;

    private String imageUrl;

    private Date createTime;

    @JsonIgnore
    private Date updateTime;
    @JsonIgnore
    private String code;
}
