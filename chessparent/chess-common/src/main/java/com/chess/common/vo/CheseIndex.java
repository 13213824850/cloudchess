package com.chess.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther:huang yuan li
 * @Description:
 * @date: Create in ${TIME} ${DATE}
 */
@Data
public class CheseIndex implements Serializable {

	//消息类型
	private int messageCode;
	private String message;
	//游戏状态
	private int gameState;
	private CodeIndex codeIndex;
	//轮到哪方
	private String turnMe;
	//红方棋子
	private String redUserName;
	private String userName;
	private String oppUserName;
	private Map<String, Object> map= new HashMap<>(); 
	public  CheseIndex add(String key,Object value) {
		map.put(key, value);
		return this;
	}
		
	
}
