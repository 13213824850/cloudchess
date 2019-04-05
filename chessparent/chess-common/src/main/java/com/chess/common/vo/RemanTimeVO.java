package com.chess.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RemanTimeVO {
    private String userName;
    //创建的过期时间
    private Instant overTime;
    //允许的时间
    private int time;
}
