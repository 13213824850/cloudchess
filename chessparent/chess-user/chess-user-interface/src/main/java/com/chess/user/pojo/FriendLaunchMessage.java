package com.chess.user.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
public class FriendLaunchMessage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String userName;

    @NotBlank
    private String nickName;

    private Byte sex;
    @NotBlank
    private String launchUserName;
    @NotBlank
    private String launchNickName;

    private Byte launchSex;

    private Integer state;

    private Date created;

    private Date updated;

    private static final long serialVersionUID = 1L;

}