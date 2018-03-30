package com.github.catalpaflat.pay.generate;

import java.util.Random;

/**
 * @author CatalpaFlat
 */
public final class RandomGenerate {
    private RandomGenerate() {
    }

    private static final String ORDER_NUM_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * 生成订单号
     *
     * @return 订单号
     */
    public static String getOrderNum(int length, String prefixStr) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int number = random.nextInt(ORDER_NUM_STR.length());
            sb.append(ORDER_NUM_STR.charAt(number));
        }

        String randomResult = sb.toString();
        String timeStr = Long.toString(System.currentTimeMillis());
        return prefixStr + timeStr + randomResult;
    }
}
