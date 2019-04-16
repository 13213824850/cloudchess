package com.chess.user.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
public class Friend implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String friendName;

    private String friendNickName;

    private Byte friendSex;

    private Integer onLine;//是否在线

    private Date created;

    private Date updated;

    private static final long serialVersionUID = 1L;


    }
