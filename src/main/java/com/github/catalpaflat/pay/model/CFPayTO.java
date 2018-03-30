package com.github.catalpaflat.pay.model;

import com.github.catalpaflat.pay.model.alipay.CFAlipayTO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author CatalpaFlat
 */
@Getter
@Setter
public class CFPayTO {
    private CFPayWXTO wx;
    private CFAlipayTO alipay;
}
