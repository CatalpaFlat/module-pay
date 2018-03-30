package com.github.catalpaflat.pay.utils;

import com.github.catalpaflat.pay.constant.EncodeConstant;

import java.security.MessageDigest;

/**
 * @author CatalpaFlat
 */
public final class SignUtil {
    private SignUtil() {
    }

    /**
     * 签名字符串
     *
     * @param text 需要签名的字符串
     * @param key  密钥
     * @return 签名结果
     */
    public static String sign(String text, String key) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        String key1 = "&key=" + key;
        text = text + key1;
        byte[] array = md.digest(text.getBytes(EncodeConstant.ENCODE_UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 验证签名字符串
     *
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key  密钥
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key) throws Exception {
        String mySign = sign(text, key);
        return mySign.equals(sign);
    }
}
