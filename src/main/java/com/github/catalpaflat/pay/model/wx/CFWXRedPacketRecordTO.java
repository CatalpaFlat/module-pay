package com.github.catalpaflat.pay.model.wx;

import lombok.Getter;
import lombok.Setter;

/**
 * @author CatalpaFlat
 */
public class CFWXRedPacketRecordTO {

    @Getter
    @Setter
    private String appid;

    @Getter
    @Setter
    private String mch_id;

    @Getter
    @Setter
    private String nonce_str;

    @Getter
    @Setter
    private String sign;

    @Getter
    @Setter
    private String mch_billno;

    @Getter
    @Setter
    private String merchant_pay_key;

    @Getter
    @Setter
    private String bill_type;
}
