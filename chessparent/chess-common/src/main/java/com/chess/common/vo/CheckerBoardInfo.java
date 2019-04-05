package com.chess.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CheckerBoardInfo implements Serializable{


	//棋颜色
	private int code;
	//棋颜色
	private String name;
	private String checkerBoardID;
	//棋盘另用户userName
	private String oppUserName;
	//棋盘另用户昵称
	//private String oppNickName;
	//棋盘状态
	private int gameState;
	//对局类型
	private int type;
	//開始時間
	private Date date;
}
