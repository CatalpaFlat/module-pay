package com.github.catalpaflat.pay.model.alipay;

import lombok.Getter;
import lombok.Setter;

/**
 * @author CatalpaFlat
 */
@Getter
@Setter
public class CFAlipayTO {
    private String app_id;
    private String partner;
    private String notify_url;
    private String private_key;
    private String alipay_public_key;
}
