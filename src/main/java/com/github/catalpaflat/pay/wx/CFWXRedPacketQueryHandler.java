package com.github.catalpaflat.pay.wx;

import com.github.catalpaflat.pay.config.CFPayConfig;
import com.github.catalpaflat.pay.generate.UriGenerate;
import com.github.catalpaflat.pay.http.HttpPostSupport;
import com.github.catalpaflat.pay.model.wx.CFWXRedPacketRecordTO;
import com.github.catalpaflat.pay.utils.MapUtil;
import com.github.catalpaflat.pay.utils.SignUtil;
import com.github.catalpaflat.pay.utils.XmlUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.catalpaflat.pay.constant.CFPayUrlConstant.WX_PAY_ORDER_QUERY_URL;

/**
 * @author CatalpaFlat
 */
public class CFWXRedPacketQueryHandler extends CFWXPayHandlerEndpoint {
    public CFWXRedPacketQueryHandler(CFPayConfig cfPayConfig) {
        super(cfPayConfig);
    }

    public Map<String, Object> detectedRedPacket(String orderNum) throws Exception {
        String substring = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
        Map<String, Object> sParaTemp = new HashMap<String, Object>(10, 10);
        sParaTemp.put("appid", officialAccountAppId);
        sParaTemp.put("mch_id", wx.getMch_id());
        sParaTemp.put("nonce_str", substring);
        sParaTemp.put("mch_billno", orderNum);
        sParaTemp.put("bill_type", "MCHT");
        CFWXRedPacketRecordTO cfwxRedPacketTO = new CFWXRedPacketRecordTO();
        cfwxRedPacketTO.setAppid(officialAccountAppId);
        cfwxRedPacketTO.setBill_type("MCHT");
        cfwxRedPacketTO.setMch_billno(orderNum);
        cfwxRedPacketTO.setMch_id(wx.getMch_id());
        cfwxRedPacketTO.setMerchant_pay_key(wx.getMerchant_pay_key());
        cfwxRedPacketTO.setNonce_str(substring);
        // 除去Map中的空值和签名参数
        Map<String, Object> sPara = MapUtil.filterBlankValueWithSpecifiedKey(sParaTemp);
        // 把Map所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String preStr = UriGenerate.createWXPayUri(sPara);
        String sign = SignUtil.sign(preStr, wx.getMerchant_pay_key());
        cfwxRedPacketTO.setSign(sign);
        cfwxRedPacketTO.setMerchant_pay_key(null);
        String respXml = XmlUtil.messageToXML(cfwxRedPacketTO);


        String result = HttpPostSupport.post(WX_PAY_ORDER_QUERY_URL, respXml);
        // 将解析结果存储在HashMap中
        Map<String, Object> map = XmlUtil.doXMLParse(result);

        return map;
    }
}
