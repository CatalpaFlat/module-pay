package com.github.catalpaflat.pay.model.alipay.info;

import lombok.Getter;
import lombok.Setter;

/**
 * @author CatalpaFlat
 */
@Getter
@Setter
public class CfAlipayQueryInfo {
    /**
     * 商户订单号
     */
    private String out_trade_no;
    /**
     * 支付宝交易号（支付时响应）
     */
    private String trade_no;

    public CfAlipayQueryInfo(String out_trade_no, String trade_no) {
        this.out_trade_no = out_trade_no;
        this.trade_no = trade_no;
    }

}
