package com.chess.auth.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 2:54 2019/3/11 0011
 * @Modifide by:
 */
public class ObjectUtils {
    public static Long toLong(Object obj) {
        if (obj == null) {
            return 0L;
        }
        if (obj instanceof Double || obj instanceof Float) {
            return Long.valueOf(StringUtils.substringBefore(obj.toString(), "."));
        }
        if (obj instanceof Number) {
            return Long.valueOf(obj.toString());
        }
        if(obj instanceof Long){
            return (Long) obj;
        }
        if (obj instanceof String) {
            return Long.valueOf(obj.toString());
        } else {
            return 0L;
        }
    }
    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }
}
