package com.github.catalpaflat.pay.model;

import com.github.catalpaflat.pay.model.wx.CFPayWXAppIdTO;
import com.github.catalpaflat.pay.model.wx.CFPayWXH5WapTO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author CatalpaFlat
 */
@Getter
@Setter
public class CFPayWXTO {
    private CFPayWXAppIdTO app_id;
    private String mch_id;
    private String merchant_pay_key;
    private String notify_url;
    private CFPayWXH5WapTO h5_info;
}
