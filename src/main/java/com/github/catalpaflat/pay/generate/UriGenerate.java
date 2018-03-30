package com.github.catalpaflat.pay.generate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author CatalpaFlat
 */
public final class UriGenerate {
    private UriGenerate() {
    }

    private static final String AND_STR = "&";
    private static final String EQUAL_STR = "=";

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params           需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createWXPayUri(Map<String, Object> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        StringBuilder preStr = new StringBuilder();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = params.get(key);
            // 拼接时，不包括最后一个&字符
            if (i == keys.size() - 1) {
                preStr = preStr.append(key).append(EQUAL_STR).append(value);
            } else {
                preStr = preStr.append(key).append(EQUAL_STR).append(value).append(AND_STR);
            }
        }

        return preStr.toString();
    }
}
