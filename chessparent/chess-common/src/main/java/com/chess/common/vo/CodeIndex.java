package com.chess.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 1:33 2019/3/22 0022
 * @Modifide by:
 */
@Data
public class CodeIndex implements Serializable {
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    //棋子code
    private int code;
}
