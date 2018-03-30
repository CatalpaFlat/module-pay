package com.github.catalpaflat.pay.constant;

/**
 * @author CatalpaFlat
 */
public final class CFPayUrlConstant {
    private CFPayUrlConstant() {
    }

    /**
     * wx统一下单url
     */
    public static final String WX_PAY_ORDER_SAME_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    /**
     * wx刷卡统一下单url
     */
    public static final String WX_PAY_CARD_ORDER_SAME_URL = "https://api.mch.weixin.qq.com/pay/micropay";
    /**
     * wx支付结果查询url
     */
    public static final String WX_PAY_ORDER_QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

    /**
     * 微信发放普通现金红包接口链接
     */
    public static final String WX_PAY_RED_PACKET_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";

    /**
     * 微信发放分裂现金红包接口链接
     */
    public static final String WX_PAY_GROUP_RED_PACKET_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendgroupredpack";


    public static final String ALI_PAY_Url = "https://openapi.alipay.com/gateway.do";
    /**
     * 支付宝消息验证地址
     */
    public static final String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";

}
