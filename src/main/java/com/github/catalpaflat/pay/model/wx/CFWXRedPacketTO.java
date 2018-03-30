package com.github.catalpaflat.pay.model.wx;

import lombok.Getter;
import lombok.Setter;

/**
 * @author CatalpaFlat
 */
@Getter
@Setter
public class CFWXRedPacketTO {
    private String wxappid;
    private String mch_id;
    private String nonce_str;
    private String sign;
    private String mch_billno;
    private String merchant_pay_key;
    private String send_name;
    private String re_openid;
    private Integer total_amount;
    private Integer total_num;
    private String wishing;
    private String client_ip;
    private String amt_type;
    private String act_name;
    private String remark;
}
