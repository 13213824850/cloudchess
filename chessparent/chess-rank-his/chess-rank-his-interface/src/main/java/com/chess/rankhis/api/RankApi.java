package com.chess.rankhis.api;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 4:09 2019/3/30 0030
 * @Modifide by:
 */
public interface RankApi {
    @GetMapping("getRankGrade")
    public Double getRankGrade(String userName);
}
