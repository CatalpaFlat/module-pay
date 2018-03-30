package com.github.catalpaflat.pay.wx;

import com.github.catalpaflat.pay.config.CFPayConfig;
import com.github.catalpaflat.pay.enums.WXPayGenreEnum;
import com.github.catalpaflat.pay.exception.CFPayException;
import com.github.catalpaflat.pay.generate.UriGenerate;
import com.github.catalpaflat.pay.http.HttpPostSupport;
import com.github.catalpaflat.pay.model.wx.CFWXPayXmlTO;
import com.github.catalpaflat.pay.model.wx.info.CFWxPayInfo;
import com.github.catalpaflat.pay.utils.MapUtil;
import com.github.catalpaflat.pay.utils.SignUtil;
import com.github.catalpaflat.pay.utils.XmlUtil;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.catalpaflat.pay.constant.CFPayConstant.*;
import static com.github.catalpaflat.pay.constant.CFPayUrlConstant.WX_PAY_CARD_ORDER_SAME_URL;
import static com.github.catalpaflat.pay.constant.CFPayUrlConstant.WX_PAY_ORDER_SAME_URL;

/**
 * @author CatalpaFlat
 */
public class CFWXPayUnifiedOrderHandler extends CFWXPayHandlerEndpoint {

    public CFWXPayUnifiedOrderHandler(CFPayConfig cfPayConfig) {
        super(cfPayConfig);
    }

    public Map<String, Object> unifiedOrder(CFWxPayInfo cfWxPayInfo) throws Exception {
        WXPayGenreEnum wxPayGenreEnum = cfWxPayInfo.getWxPayGenreEnum();
        String appId = getAppId(wxPayGenreEnum);
        CFWXPayXmlTO cfwxPayXmlTO = obtainCFWXPayXmlTO(cfWxPayInfo, appId);
        Map<String, Object> sParaTemp = packageToMap(cfwxPayXmlTO, appId, wxPayGenreEnum);
        // 除去Map中的空值和签名参数
        Map<String, Object> sPara = MapUtil.filterBlankValueWithSpecifiedKey(sParaTemp);
        // 把Map所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String preStr = UriGenerate.createWXPayUri(sPara);
        String sign = SignUtil.sign(preStr, wx.getMerchant_pay_key());
        cfwxPayXmlTO.setSign(sign);
        cfwxPayXmlTO.setMerchant_pay_key(null);
        String respXml = XmlUtil.messageToXML(cfwxPayXmlTO);
        // 打印respXml发现，得到的xml中有“__”不对，应该替换成“_”
        respXml = respXml.replaceAll("__", "_");

        String result;
        //刷卡支付的下单地址不一样
        if (wxPayGenreEnum.getValue().equals(WXPayGenreEnum.SLOT_CARD.getValue())) {
            result = HttpPostSupport.post(WX_PAY_CARD_ORDER_SAME_URL, respXml);
        } else {
            result = HttpPostSupport.post(WX_PAY_ORDER_SAME_URL, respXml);
        }
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

        if (!(PAY_SUCCESS_STR.equals(returnCode) || returnCode.equals(resultCode))) {
            if (StringUtils.isNotBlank(returnMsg)) {
                return map;
            }
        }

        // 业务结果 返回的预付单信息
        String prepayId = (String) map.get("prepay_id");
        String nonceStr2 = UUID.randomUUID().toString().replaceAll("-", "");
        responseMap.put("nonceStr", nonceStr2);
        responseMap.put("package", "prepay_id=" + prepayId);
        Long timeStamp = System.currentTimeMillis() / 1000;
        responseMap.put("timeStamp", timeStamp + "");
        String stringSignTemp;
        //app 支付再次签名
        if (cfWxPayInfo.getTradeType().equals(WX_PAY_TRADE_TYPE_APP)) {
            stringSignTemp = "appid=" + appId + "&partnerid=" + wx.getMch_id() + "&prepay_id=" + prepayId + "&package=Sign=WXPay&noncestr=" + nonceStr2 + "&timestamp=" + timeStamp;
            responseMap.put("appid", appId);
            responseMap.put("partnerid", wx.getMch_id());
            responseMap.put("prepayid", prepayId);
        } else {
            //小程序和公众号再次签名
            stringSignTemp = "appId=" + appId + "&nonceStr=" + nonceStr2 + "&package=prepay_id=" + prepayId + "&signType=MD5&timeStamp=" + timeStamp;
        }

        //再次签名
        String paySign = SignUtil.sign(stringSignTemp, wx.getMerchant_pay_key());
        responseMap.put("paySign", paySign);
        return responseMap;
    }

    private CFWXPayXmlTO obtainCFWXPayXmlTO(CFWxPayInfo cfWxPayInfo, String appId) {
        CFWXPayXmlTO cfwxPayXmlTO = new CFWXPayXmlTO();
        WXPayGenreEnum wxPayGenreEnum = cfWxPayInfo.getWxPayGenreEnum();
        Integer value = wxPayGenreEnum.getValue();
        if (!value.equals(WXPayGenreEnum.SLOT_CARD.getValue())) {
            cfwxPayXmlTO.setNotify_url(wx.getNotify_url());
            cfwxPayXmlTO.setOpenid(cfWxPayInfo.getOpenid());
            cfwxPayXmlTO.setTrade_type(cfWxPayInfo.getTradeType());
        }
        if (value.equals(WXPayGenreEnum.SLOT_CARD.getValue())) {
            if (cfWxPayInfo.getAuthCode() == null) {
                throw new CFPayException("wx pay h5_info is empty");
            }
            cfwxPayXmlTO.setAuth_code(cfWxPayInfo.getAuthCode());
        }

        if (value.equals(WXPayGenreEnum.H5.getValue())) {
            if (cfPayWXH5InfoTO == null) {
                throw new CFPayException("wx pay h5_info is empty");
            }
            cfwxPayXmlTO.setScene_info(cfPayWXH5InfoTO);
        }
        cfwxPayXmlTO.setAppid(appId);
        cfwxPayXmlTO.setTotal_fee(cfWxPayInfo.getTotalFee());
        cfwxPayXmlTO.setBody(cfWxPayInfo.getBody());
        cfwxPayXmlTO.setGoods_tag(cfWxPayInfo.getGoodsTag());
        cfwxPayXmlTO.setMch_id(wx.getMch_id());
        cfwxPayXmlTO.setOut_trade_no(cfWxPayInfo.getOrderNum());
        cfwxPayXmlTO.setSpbill_create_ip(cfWxPayInfo.getTerminalIp());
        cfwxPayXmlTO.setNonce_str(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32));

        return cfwxPayXmlTO;
    }

    /**
     * 打包为 Map
     */
    private Map<String, Object> packageToMap(CFWXPayXmlTO cfwxPayXmlTO, String appId, WXPayGenreEnum wxPayGenreEnum) {
        Map<String, Object> sParaTemp = new HashMap<String, Object>(10, 10);
        sParaTemp.put("appid", appId);
        sParaTemp.put("mch_id", wx.getMch_id());
        sParaTemp.put("nonce_str", cfwxPayXmlTO.getNonce_str());
        sParaTemp.put("body", cfwxPayXmlTO.getBody());
        sParaTemp.put("out_trade_no", cfwxPayXmlTO.getOut_trade_no());
        sParaTemp.put("total_fee", cfwxPayXmlTO.getTotal_fee());
        sParaTemp.put("spbill_create_ip", cfwxPayXmlTO.getSpbill_create_ip());
        Integer value = wxPayGenreEnum.getValue();
        //刷卡支付可能没有该字段
        if (!value.equals(WXPayGenreEnum.SLOT_CARD.getValue())) {
            sParaTemp.put("notify_url", wx.getNotify_url());
            sParaTemp.put("openid", cfwxPayXmlTO.getOpenid());
            sParaTemp.put("trade_type", cfwxPayXmlTO.getTrade_type());
        }

        //刷卡支付才有的 auth_code
        if (value.equals(WXPayGenreEnum.SLOT_CARD.getValue())) {
            sParaTemp.put("auth_code", cfwxPayXmlTO.getAuth_code());
        }

        //H5支付才有的场景对象
        if (value.equals(WXPayGenreEnum.H5.getValue())) {
            sParaTemp.put("scene_info", cfPayWXH5InfoTO);
        }
        return sParaTemp;
    }


}
