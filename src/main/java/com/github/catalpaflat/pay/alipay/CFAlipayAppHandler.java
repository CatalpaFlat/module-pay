package com.github.catalpaflat.pay.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.github.catalpaflat.pay.config.CFPayConfig;
import com.github.catalpaflat.pay.exception.CFPayException;
import com.github.catalpaflat.pay.model.alipay.info.CFAlipayCreateInfo;

import java.math.RoundingMode;

/**
 * @author CatalpaFlat
 */
public class CFAlipayAppHandler extends CFAlipayHandlerEndpoint {
    public CFAlipayAppHandler(CFPayConfig cfPayConfig) {
        super(cfPayConfig);
    }

    /**
     * app支付宝支付
     *
     * @param cfAlipayCreateInfo 请求参数
     * @return aoo支付响应
     */
    public AlipayTradeAppPayResponse aliAppPay(CFAlipayCreateInfo cfAlipayCreateInfo) {
        // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();

        // SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(cfAlipayCreateInfo.getBody());
        model.setSubject(cfAlipayCreateInfo.getSubject());
        model.setOutTradeNo(cfAlipayCreateInfo.getBillNo());
        model.setTimeoutExpress(overTime);
        model.setTotalAmount(String.valueOf(cfAlipayCreateInfo.getTotalAmount().setScale(2, RoundingMode.HALF_EVEN)));
        model.setProductCode(productCode);

        request.setBizModel(model);
        request.setNotifyUrl(notify_url);
        // 这里和普通的接口调用不同，使用的是sdkExecute
        try {
            return alipayClient.sdkExecute(request);
        } catch (AlipayApiException e) {
            throw new CFPayException(e.getMessage());
        }
    }
}
