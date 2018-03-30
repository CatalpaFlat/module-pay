package com.github.catalpaflat.pay.model.wx;

import lombok.Getter;
import lombok.Setter;

/**
 * @author CatalpaFlat
 */
@Getter
@Setter
public class CFWXPayQueryTO {
    private String appid;

    private String mch_id;

    private String nonce_str;

    private String sign;

    private String out_trade_no;

    private String merchant_pay_key;

    public CFWXPayQueryTO(String appid, String mch_id, String nonce_str, String sign, String out_trade_no) {
        this.appid = appid;
        this.mch_id = mch_id;
        this.nonce_str = nonce_str;
        this.sign = sign;
        this.out_trade_no = out_trade_no;
    }
}
