package com.github.catalpaflat.pay.wx;

import com.github.catalpaflat.pay.config.CFPayConfig;
import com.github.catalpaflat.pay.enums.WXRebPacketEnum;
import com.github.catalpaflat.pay.generate.UriGenerate;
import com.github.catalpaflat.pay.http.HttpPostSupport;
import com.github.catalpaflat.pay.model.wx.CFWXRedPacketTO;
import com.github.catalpaflat.pay.model.wx.info.CFWXRedPacketInfo;
import com.github.catalpaflat.pay.utils.MapUtil;
import com.github.catalpaflat.pay.utils.SignUtil;
import com.github.catalpaflat.pay.utils.XmlUtil;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.catalpaflat.pay.constant.CFPayConstant.PAY_ERROR_STATE;
import static com.github.catalpaflat.pay.constant.CFPayConstant.PAY_SUCCESS_STR;
import static com.github.catalpaflat.pay.constant.CFPayUrlConstant.WX_PAY_GROUP_RED_PACKET_URL;
import static com.github.catalpaflat.pay.constant.CFPayUrlConstant.WX_PAY_RED_PACKET_URL;

/**
 * @author CatalpaFlat
 */
public class CFWXRedPacketSendHandler extends CFWXPayHandlerEndpoint {
    public CFWXRedPacketSendHandler(CFPayConfig cfPayConfig) {
        super(cfPayConfig);
    }

    public Map<String, Object> sendReadPacket(CFWXRedPacketInfo cfwxRedPacketInfo) throws Exception {

        CFWXRedPacketTO cfwxRedPacketTO = obtainCFWXRedPacketTO(cfwxRedPacketInfo);
        Map<String, Object> sParaTemp = redPacketPackageToMap(cfwxRedPacketTO);
        // 除去Map中的空值和签名参数
        Map<String, Object> sPara = MapUtil.filterBlankValueWithSpecifiedKey(sParaTemp);
        // 把Map所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String preStr = UriGenerate.createWXPayUri(sPara);
        String sign = SignUtil.sign(preStr, wx.getMerchant_pay_key());
        cfwxRedPacketTO.setSign(sign);
        cfwxRedPacketTO.setMerchant_pay_key(null);
        String respXml = XmlUtil.messageToXML(cfwxRedPacketTO);

        // 打印respXml发现，得到的xml中有“__”不对，应该替换成“_”
        respXml = respXml.replaceAll("__", "_");

        String result;
        //红包金额设置方式不为空为发送分裂红包
        if (StringUtils.isNotBlank(cfwxRedPacketTO.getAmt_type())) {
            result = HttpPostSupport.post(WX_PAY_GROUP_RED_PACKET_URL, respXml);
        } else {
            result = HttpPostSupport.post(WX_PAY_RED_PACKET_URL, respXml);
        }

        // 将解析结果存储在HashMap中
        Map<String, Object> map = XmlUtil.doXMLParse(result);
        // 返回信息 返回状态码 返回信息
        String returnCode = (String) map.get("return_code");
        String returnMsg = (String) map.get("return_msg");
        String resultCode = (String) map.get("result_code");

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
        //请求成功
        if (PAY_SUCCESS_STR.equals(returnCode) && PAY_SUCCESS_STR.equals(resultCode)) {
            responseMap.put("result", map);
        }
        return responseMap;
    }

    private Map<String, Object> redPacketPackageToMap(CFWXRedPacketTO cfwxRedPacketTO) {
        Map<String, Object> sParaTemp = new HashMap<String, Object>(10, 10);
        sParaTemp.put("wxappid", cfwxRedPacketTO.getWxappid());
        sParaTemp.put("mch_id", cfwxRedPacketTO.getMch_id());
        sParaTemp.put("nonce_str", cfwxRedPacketTO.getNonce_str());
        sParaTemp.put("send_name", cfwxRedPacketTO.getSend_name());
        sParaTemp.put("re_openid", cfwxRedPacketTO.getRe_openid());
        sParaTemp.put("total_amount", cfwxRedPacketTO.getTotal_amount());
        sParaTemp.put("total_num", cfwxRedPacketTO.getTotal_num());
        sParaTemp.put("wishing", cfwxRedPacketTO.getWishing());
        sParaTemp.put("client_ip", cfwxRedPacketTO.getClient_ip());
        sParaTemp.put("act_name", cfwxRedPacketTO.getAct_name());
        sParaTemp.put("remark", cfwxRedPacketTO.getRemark());
        if (StringUtils.isNotBlank(cfwxRedPacketTO.getAmt_type())) {
            sParaTemp.put("amt_type", cfwxRedPacketTO.getAmt_type());
        }
        return sParaTemp;
    }


    private CFWXRedPacketTO obtainCFWXRedPacketTO(CFWXRedPacketInfo cfwxRedPacketInfo) {
        CFWXRedPacketTO cfwxRedPacketTO = new CFWXRedPacketTO();
        cfwxRedPacketTO.setWxappid(officialAccountAppId);
        cfwxRedPacketTO.setRe_openid(cfwxRedPacketInfo.getOpenid());
        cfwxRedPacketTO.setAct_name(cfwxRedPacketInfo.getActName());
        cfwxRedPacketTO.setClient_ip(cfwxRedPacketInfo.getClientIp());
        cfwxRedPacketTO.setMch_billno(cfwxRedPacketInfo.getOrderNum());
        cfwxRedPacketTO.setMch_id(wx.getMch_id());
        cfwxRedPacketTO.setMerchant_pay_key(wx.getMerchant_pay_key());
        cfwxRedPacketTO.setNonce_str(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32));
        cfwxRedPacketTO.setSend_name(cfwxRedPacketInfo.getSendName());
        cfwxRedPacketTO.setRemark(cfwxRedPacketInfo.getRemark());
        cfwxRedPacketTO.setTotal_amount(cfwxRedPacketInfo.getTotalAmount());
        cfwxRedPacketTO.setTotal_num(cfwxRedPacketInfo.getTotalNum());
        cfwxRedPacketTO.setWishing(cfwxRedPacketInfo.getWishing());

        WXRebPacketEnum wxRebPacketEnum = cfwxRedPacketInfo.getWxRebPacketEnum();
        if (wxRebPacketEnum.equals(WXRebPacketEnum.FISSION)) {
            cfwxRedPacketTO.setAmt_type(cfwxRedPacketInfo.getAmtType());
        }
        return cfwxRedPacketTO;
    }
}
