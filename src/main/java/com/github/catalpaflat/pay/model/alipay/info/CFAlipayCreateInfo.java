package com.github.catalpaflat.pay.model.alipay.info;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author CatalpaFlat
 */
@Getter
@Setter
public class CFAlipayCreateInfo {
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
}
