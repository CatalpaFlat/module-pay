package com.github.catalpaflat.pay.wx;

import com.github.catalpaflat.pay.config.CFPayConfig;
import com.github.catalpaflat.pay.enums.WXPayGenreEnum;
import com.github.catalpaflat.pay.generate.UriGenerate;
import com.github.catalpaflat.pay.http.HttpPostSupport;
import com.github.catalpaflat.pay.model.wx.CFWXPayQueryTO;
import com.github.catalpaflat.pay.utils.MapUtil;
import com.github.catalpaflat.pay.utils.SignUtil;
import com.github.catalpaflat.pay.utils.XmlUtil;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.catalpaflat.pay.constant.CFPayConstant.PAY_ERROR_STATE;
import static com.github.catalpaflat.pay.constant.CFPayConstant.PAY_SUCCESS_STR;
import static com.github.catalpaflat.pay.constant.CFPayUrlConstant.WX_PAY_ORDER_QUERY_URL;

/**
 * @author CatalpaFlat
 */
public class CFWXPayOrderStatusQueryHandler extends CFWXPayHandlerEndpoint {
    public CFWXPayOrderStatusQueryHandler(CFPayConfig cfPayConfig) {
        super(cfPayConfig);
    }

    public Map<String, Object> obtainOrderStatusByOrderNum(String orderNum, WXPayGenreEnum wxPayGenreEnum) throws Exception {
        String appId = getAppId(wxPayGenreEnum);
        String substring = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
        Map<String, Object> sParaTemp = new HashMap<String, Object>(10, 10);
        //支付查询是通用的，只需要这 4 个参数和他们的签名
        sParaTemp.put("appid", appId);
        sParaTemp.put("mch_id", wx.getMch_id());
        sParaTemp.put("nonce_str", substring);
        sParaTemp.put("out_trade_no", orderNum);

        // 除去Map中的空值和签名参数
        Map<String, Object> sPara = MapUtil.filterBlankValueWithSpecifiedKey(sParaTemp);
        // 把Map所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String preStr = UriGenerate.createWXPayUri(sPara);
        String sign = SignUtil.sign(preStr, wx.getMerchant_pay_key());
        CFWXPayQueryTO cfwxPayQueryTO = new CFWXPayQueryTO(appId, wx.getMch_id(), substring, sign, orderNum);

        String respXml = XmlUtil.messageToXML(cfwxPayQueryTO);
        // 打印respXml发现，得到的xml中有“__”不对，应该替换成“_”
        respXml = respXml.replaceAll("__", "_");

        String result = HttpPostSupport.post(WX_PAY_ORDER_QUERY_URL, respXml);
        // 将解析结果存储在HashMap中
        Map<String, Object> map = XmlUtil.doXMLParse(result);
        // 返回信息 返回状态码 返回信息
        String returnCode = (String) map.get("return_code");
        String resultCode = (String) map.get("result_code");
        String returnMsg = (String) map.get("return_msg");
        Map<String, Object> responseMap = new HashMap<String, Object>(4, 4);

        // resultCode 或 returnCode 为 FAIL，调取微信接口失败，return_msg 不为空则为错误信息
        if (StringUtils.equals(resultCode, PAY_ERROR_STATE) || StringUtils.equals(returnCode, PAY_ERROR_STATE)) {
            if (StringUtils.isNotBlank(returnMsg)) {
                return map;
            }
        }

        //支付状态
        String tradeState = (String) map.get("trade_state");

        //未完成支付
        if (!tradeState.equals(PAY_SUCCESS_STR)) {
            responseMap.put("trade_state", tradeState);
            responseMap.put("message", map.get("trade_state_desc"));
        }
        //支付成功
        if (PAY_SUCCESS_STR.equals(returnCode) && returnCode.equals(resultCode) && tradeState.equals(resultCode)) {
            responseMap.put("trade_state", tradeState);
            responseMap.put("total_fee", map.get("total_fee"));
            responseMap.put("order_num", map.get("out_trade_no"));
        }

        return responseMap;
    }
}
