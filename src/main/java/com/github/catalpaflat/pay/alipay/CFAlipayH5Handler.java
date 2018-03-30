package com.github.catalpaflat.pay.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.github.catalpaflat.pay.config.CFPayConfig;
import com.github.catalpaflat.pay.exception.CFPayException;
import com.github.catalpaflat.pay.model.alipay.info.CFAlipayH5CreateInfo;

import java.math.RoundingMode;

/**
 * @author CatalpaFlat
 */
public class CFAlipayH5Handler extends CFAlipayHandlerEndpoint {
    public CFAlipayH5Handler(CFPayConfig cfPayConfig) {
        super(cfPayConfig);
    }

    /**
     * H5支付
     *
     * @param cfAlipayCreateInfo 支付信息
     * @return 支付结果
     */
    public AlipayTradeWapPayResponse aliH5Pay(CFAlipayH5CreateInfo cfAlipayCreateInfo) {
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        AlipayTradeWapPayModel alipayTradeWapPayModel = new AlipayTradeWapPayModel();
        alipayTradeWapPayModel.setSubject(cfAlipayCreateInfo.getSubject());
        alipayTradeWapPayModel.setBody(cfAlipayCreateInfo.getBody());
        alipayTradeWapPayModel.setOutTradeNo(cfAlipayCreateInfo.getBillNo());
        alipayTradeWapPayModel.setTotalAmount(String.valueOf(cfAlipayCreateInfo.getTotalAmount().setScale(2, RoundingMode.HALF_EVEN)));
        alipayTradeWapPayModel.setSellerId(partner);
        alipayTradeWapPayModel.setProductCode(productCodeH5);

        alipayRequest.setNotifyUrl(notify_url);
        alipayRequest.setReturnUrl(cfAlipayCreateInfo.getReturnUrl());
        alipayRequest.setBizModel(alipayTradeWapPayModel);

        try {
            return alipayClient.pageExecute(alipayRequest);
        } catch (AlipayApiException e) {
            throw new CFPayException(e.getMessage());
        }
    }
}
