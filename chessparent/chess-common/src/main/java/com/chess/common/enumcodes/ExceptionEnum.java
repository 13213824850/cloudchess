package com.chess.common.enumcodes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ExceptionEnum {
	ALERYEXIT(101,"用户已存在"),
	NOAGAINSEND(102,"请勿5分钟内连续注册"),
	INVALIDLINE(103,"无效的链接"),
	NOTFINDUSER(104, "未找到用户"),
	CREATETOKENFAIL(105 , "生成token失败"),
	UNINVALID(106, "无效的token"),
	GETPRIFAIL(107, "获取私钥失败"),
	PARAMVALIDFAIL(110, "数据校验失败"),
	NICKNAME_NOT_EMPTY(111,"昵称不能为空"),
	ALERDY_EXIT_NICKNAME(112,"用户昵称已存在"),
	MATCHOVERTIME(201,"对局已过期"),
	REFUSE_MATCH(202,"玩家拒绝"),
	CUT_SERVER(400, "与服务器断开连接"),
	NOT_ONLINE(401, "对方不在线"),
	FRIEND_PLAYING(402,"对方游戏中"),
	FRIEND_MATCH(403, "对方匹配中"),
	WE_PLAYING(405, "已在匹配中请勿重复"),
	CONFIRM_OVER_TIME(406,"确认超时"),
	NOT_FIND_FRIEND(407, "未找到好友"),
	LAUNCH_FRIEND_ALERDY_EXIT(408,"添加好友信息已存在，请勿重复添加"),
;
	//异常状态码和消息
	private int code;
	private String message;
}
