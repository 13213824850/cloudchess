package com.chess.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 2:32 2019/3/22 0022
 * @Modifide by:
 */
@Data
@AllArgsConstructor
public class MatchMessage {
    //200success 400fail
    private int code;
    private String message;
}
