package com.github.catalpaflat.pay.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.github.catalpaflat.pay.config.CFPayConfig;
import com.github.catalpaflat.pay.exception.CFPayException;
import com.github.catalpaflat.pay.model.alipay.info.CfAlipayQueryInfo;
import net.sf.json.JSONObject;

import static com.github.catalpaflat.pay.constant.CFPayConstant.ALI_PAY_OUT_TRADE_NO;
import static com.github.catalpaflat.pay.constant.CFPayConstant.ALI_PAY_TRADE_NO;

/**
 * @author CatalpaFlat
 */
public class CFAlipayQueryHandler extends CFAlipayHandlerEndpoint {
    public CFAlipayQueryHandler(CFPayConfig cfPayConfig) {
        super(cfPayConfig);
    }


    /**
     * 查询支付结果
     *
     * @param cfAlipayQueryInfo 请求参数
     * @return app支付结果
     */
    public AlipayTradeQueryResponse queryPayResult(CfAlipayQueryInfo cfAlipayQueryInfo) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject requestJson = new JSONObject();
        requestJson.put(ALI_PAY_OUT_TRADE_NO, cfAlipayQueryInfo.getOut_trade_no());
        requestJson.put(ALI_PAY_TRADE_NO, cfAlipayQueryInfo.getTrade_no());
        request.setBizContent(requestJson.toString());
        try {
            return alipayClient.execute(request);
        } catch (AlipayApiException e) {
            throw new CFPayException(e.getMessage());
        }
    }
}
