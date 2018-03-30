package com.github.catalpaflat.pay.model.wx;

import lombok.Getter;
import lombok.Setter;

/**
 * @author CatalpaFlat
 */
@Getter
@Setter
public class CFWXPayXmlTO {

    private String total_fee;

    private String appid;

    private String mch_id;

    private String merchant_pay_key;

    private String openid;

    private String spbill_create_ip;

    private String notify_url;

    private String nonce_str;

    private String sign;

    private String body;

    private String out_trade_no;

    private String goods_tag;

    private String trade_type;

    private String time_start;

    private String time_expire;

    private CFPayWXH5InfoTO scene_info;

    private String auth_code;

}
