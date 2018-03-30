package com.github.catalpaflat.pay.config;

import com.github.catalpaflat.pay.model.CFPayTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author CatalpaFlat
 */
@ConfigurationProperties(prefix = "catalpaflat")
public class CFPayConfig {

    @Getter
    @Setter
    private CFPayTO pay;
    /*
        catalpaflat：
            pay:
                wx:
                    mch_id:
                    merchant_pay_key:
                    notify_url:
                    app_ids:
                        app:
                        mini:
                        h5:
                        official_account:
                        slot_card:
                        scan_code:
                    h5_info:
                        type: 场景类型:Wap
                        wap_url: WAP网站URL地址:https://pay.qq.com
                        wap_name: WAP 网站名:腾讯充值
                alipay:
                    app_id:
                    private_key:
                    alipay_public_key:
                    partner:
                    notify_url:

     */
}
