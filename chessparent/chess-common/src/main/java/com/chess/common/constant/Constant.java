package com.chess.common.constant;

public class Constant {

	//存入用户信息到 redis中的前缀
	public static final String PRE_USER_REDIS = "user:";
	//存入客户端cookie 对应redis用户信息key
	public static final String USER_COOKIE = "usercookie";
	//棋盘存入redis的key
	public static final String CHECKERBOARD_REDIS_ID = "checkerboardID:";
	//棋盘 用户信息
	public static final String CHECKBOARD_INFO = "CheckerBoardInfo:";
	public static final int PageSize = 5;
	//游戏用户在线
	public static final String ONLINE = "online:";
	//游戏对局列表
	public static final String GameList = "GameList:";
	//匹配对局放入redis的key
	public static final  String MATCH_GAME_KEY  = "matchgamekey";
	//排位对局放入redis的key
	public static final  String RANK_GAME_KEY  = "rankgamekey";
	//将在线用户放入reids的key 0在线 1匹配中 2游戏中
	public static final String KEEP_ALIVE = "keepalive:";
	//所有用戶下棋超時管理用于set集合key
	public static final String REMAN_TIME = "remantime:";
	//用户超时管理
	public static final String SINGLE_OVER_TIME = "singleovertime:";
	//离线
	public static final int LINE_OFF = 0;
	//在线
	public static final int LINE_ON = 1;
	//匹配中
	public static final int LINE_MATCH = 2;
	//对剧中
	public static final int LINE_PLAY = 3;
	//好友确认
	public static final int FRIEND_AGREE = 1;
	public static final int FRIEND_REFUSE = 2;
	public static final int FRIEND_ING = 0;
}
