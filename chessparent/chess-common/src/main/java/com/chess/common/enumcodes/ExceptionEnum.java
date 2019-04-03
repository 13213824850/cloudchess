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
	MATCHOVERTIME(201,"对局已过期"),
	REFUSE_MATCH(202,"玩家拒绝"),
;
	//异常状态码和消息
	private int code;
	private String message;
}
