package com.chess.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: huang yuan li
 * @Description: 此类用作匹配参数， 数据放在set集合 以此类为key 重写equals和hashCode方法
 * @date: Create in 下午 3:12 2019/3/22 0022
 * @Modifide by:
 */
@Data
@AllArgsConstructor
public class MatchInfo implements Serializable {
    private String userName;
    private Double grade;
    private Date date;


    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (userName == null ? 0 : userName.hashCode());
        result = 31 * result + (grade == null ? 0 : grade.hashCode());
        result = 31 * result + (date == null ? 0 : date.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (object instanceof MatchInfo) {
            MatchInfo mi = (MatchInfo) object;
            if (mi.getGrade() == this.getGrade() && equalStr(mi.getUserName(), this.getUserName())
                    && this.getDate().equals(mi.getDate())) {
                return true;
            }
        }
        return false;

    }

    public boolean equalStr(String str1, String str2) {
        if (StringUtils.isEmpty(str1) && StringUtils.isEmpty(str2)) {
            return true;
        }
        if (!StringUtils.isEmpty(str1) && str1.equals(str2)) {
            return true;
        }
        return false;
    }
}
