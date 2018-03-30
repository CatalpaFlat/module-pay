package com.github.catalpaflat.pay.constant;

/**
 * @author CatalpaFlat
 */
public final class CFPayConstant {
    private CFPayConstant() {
    }

    /**
     * 统一下单API接口参数
     */
    public static final String WX_PAY_DEFAULT_GOODS_DESCEIBE = "用户充值-微信支付";
    public static final String WX_PAY_DEFAULT_TRADE_TYPE = "JSAPI";
    public static final String WX_PAY_TRADE_TYPE_SCAN = "NATIVE";
    public static final String WX_PAY_TRADE_TYPE_MWEB = "MWEB";
    public static final String WX_PAY_TRADE_TYPE_APP = "APP";
    public static final String PAY_SUCCESS_STR = "SUCCESS";
    public static final String PAY_ERROR_STATE = "FAIL";


    public static final String ALI_PAY_OUT_TRADE_NO = "out_trade_no";
    public static final String ALI_PAY_TRADE_NO = "trade_no";
}
