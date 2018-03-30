package com.github.catalpaflat.pay.alipay;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.github.catalpaflat.pay.config.CFPayConfig;
import com.github.catalpaflat.pay.constant.EncodeConstant;
import com.github.catalpaflat.pay.model.CFPayTO;
import com.github.catalpaflat.pay.model.alipay.CFAlipayTO;
import lombok.Getter;
import lombok.Setter;

import static com.github.catalpaflat.pay.constant.CFPayUrlConstant.ALI_PAY_Url;

/**
 * @author CatalpaFlat
 */
@Getter
@Setter
public class CFAlipayHandlerEndpoint {
    private CFPayConfig cfPayConfig;
    protected String app_id;
    protected String partner;
    protected String notify_url;
    protected String private_key;
    protected String alipay_public_key;
    /**
     * 加密方式
     */
    protected String encryptionMethod = "RSA2";
    /**
     * 格式
     */
    protected String format = "json";
    /**
     * 超时时间
     */
    protected String overTime = "30m";
    protected String productCode = "QUICK_MSECURITY_PAY";
    protected String productCodeH5 = "QUICK_WAP_WAY";
    protected AlipayClient alipayClient = null;

    public CFAlipayHandlerEndpoint(CFPayConfig cfPayConfig) {
        this.cfPayConfig = cfPayConfig;
        initAliPayInfo(cfPayConfig);
        initAlipayClient();
    }

    private void initAlipayClient() {
        if (alipayClient == null) {
            alipayClient = new DefaultAlipayClient(ALI_PAY_Url, app_id,
                    private_key, format, EncodeConstant.ENCODE_UTF_8, alipay_public_key,
                    encryptionMethod);
        }

    }

    private void initAliPayInfo(CFPayConfig cfPayConfig) {
        CFPayTO pay = cfPayConfig.getPay();
        CFAlipayTO alipay = pay.getAlipay();
        this.app_id = alipay.getApp_id();
        this.partner = alipay.getPartner();
        this.notify_url = alipay.getNotify_url();
        this.private_key = alipay.getPrivate_key();
        this.alipay_public_key = alipay.getAlipay_public_key();
    }

    public CFAlipayHandlerEndpoint setEncryptionMethod(String encryptionMethod) {
        this.encryptionMethod = encryptionMethod;
        return this;
    }

    public CFAlipayHandlerEndpoint setFormat(String format) {
        this.format = format;
        return this;
    }

    public CFAlipayHandlerEndpoint setOverTime(String overTime) {
        this.overTime = overTime;
        return this;
    }
}
