package com.github.catalpaflat.pay.utils;

import java.util.Map;

/**
 * @author CatalpaFlat
 */
public final class MapUtil {
    private MapUtil() {
    }

    private static final String VALUE_BLANK = "";


    /**
     * 除去Map中的空值和指定参数
     *
     * @param sArray   参数数组
     * @param strArray 字符串数组
     * @return 结果
     */
    public static Map<String, Object> filterBlankValueWithSpecifiedKey(Map<String, Object> sArray, String... strArray) {
        if (sArray.size() <= 0) {
            return sArray;
        }
        //去除指定key
        for (String str : strArray) {
            for (Map.Entry<String, Object> entry : sArray.entrySet()) {
                String key = entry.getKey();
                if (str.equals(key)) {
                    sArray.remove(key);
                    break;
                }
            }
        }
        //去除empty-value
        for (Map.Entry<String, Object> entry : sArray.entrySet()) {
            String key = entry.getKey();
            Object value = sArray.get(key);
            if (value == null || value == VALUE_BLANK) {
                sArray.remove(key);
            }
        }
        return sArray;
    }
}
