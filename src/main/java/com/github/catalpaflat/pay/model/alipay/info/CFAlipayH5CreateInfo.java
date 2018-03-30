package com.github.catalpaflat.pay.model.alipay.info;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author CatalpaFlat
 */
@Getter
@Setter
public class CFAlipayH5CreateInfo {
    /**
     * 唯一订单号
     */
    private String billNo;
    /**
     * 商品名称
     */
    private String subject;
    /**
     * 商品详情
     */
    private String body;
    /**
     * 商品金额
     */
    private BigDecimal totalAmount;

    private String returnUrl;

    public CFAlipayH5CreateInfo(String billNo, String subject, String body, BigDecimal totalAmount) {
        this.billNo = billNo;
        this.subject = subject;
        this.body = body;
        this.totalAmount = totalAmount;
    }

    public CFAlipayH5CreateInfo setReturnUrl(String rreturnUrl) {
        this.returnUrl = rreturnUrl;
        return this;
    }
}
