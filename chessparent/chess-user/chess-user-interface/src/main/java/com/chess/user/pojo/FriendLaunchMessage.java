package com.chess.user.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "friendlaunchmessage")
public class FriendLaunchMessage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //用户名
    @NotBlank
    private String userName;
    //用户昵称
    @NotBlank
    private String nickName;
    //用户性别
    private Byte sex;

    //发起好友请求的用户名
    @NotBlank
    private String launchUserName;
    //发起好友请求用户昵称
    @NotBlank
    private String launchNickName;
    //发起好友请求的用户性别
    private Byte launchSex;

    private Integer state;

    private Date created;

    private Date updated;

    private static final long serialVersionUID = 1L;

}