package com.github.catalpaflat.pay.wx;

import com.github.catalpaflat.pay.config.CFPayConfig;
import com.github.catalpaflat.pay.enums.WXPayGenreEnum;
import com.github.catalpaflat.pay.exception.CFPayException;
import com.github.catalpaflat.pay.model.CFPayTO;
import com.github.catalpaflat.pay.model.CFPayWXTO;
import com.github.catalpaflat.pay.model.wx.CFPayWXAppIdTO;
import com.github.catalpaflat.pay.model.wx.CFPayWXH5InfoTO;
import com.github.catalpaflat.pay.model.wx.CFPayWXH5WapTO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author CatalpaFlat
 */
public class CFWXPayHandlerEndpoint {

    private CFPayConfig cfPayConfig;
    @Getter
    @Setter
    protected CFPayWXTO wx;
    @Getter
    @Setter
    protected String appAppId;
    @Getter
    @Setter
    protected String miniAppId;
    @Getter
    @Setter
    protected String h5AppId;
    @Getter
    @Setter
    protected String officialAccountAppId;
    @Getter
    @Setter
    protected String slotCardAppId;
    @Getter
    @Setter
    protected String scanCodeAppId;

    @Getter
    @Setter
    protected String h5InfoType;
    @Getter
    @Setter
    protected String h5InfoWapName;
    @Getter
    @Setter
    protected String h5InfoWapUrl;
    @Getter
    @Setter
    protected CFPayWXH5InfoTO cfPayWXH5InfoTO;

    public CFWXPayHandlerEndpoint(CFPayConfig cfPayConfig) {
        this.cfPayConfig = cfPayConfig;
        CFPayTO pay = cfPayConfig.getPay();
        this.wx = pay.getWx();
        initAppIds(wx.getApp_id());
        CFPayWXH5WapTO cfPayWXH5WapTO = wx.getH5_info();
        initH5Info(cfPayWXH5WapTO);
    }

    private void initH5Info(CFPayWXH5WapTO cfPayWXH5WapTO) {
        if (cfPayWXH5WapTO == null) {
            return;
        }
        this.h5InfoType = cfPayWXH5WapTO.getType();
        this.h5InfoWapName = cfPayWXH5WapTO.getWap_name();
        this.h5InfoWapUrl = cfPayWXH5WapTO.getWap_url();
        CFPayWXH5WapTO cfPayWXH5WapTO1 = new CFPayWXH5WapTO(h5InfoType, h5InfoWapUrl, h5InfoWapName);
        cfPayWXH5InfoTO = new CFPayWXH5InfoTO(cfPayWXH5WapTO1);


    }

    private void initAppIds(CFPayWXAppIdTO appId) {
        if (appId == null) {
            throw new CFPayException("appId is empty");
        }
        this.appAppId = appId.getApp();
        this.miniAppId = appId.getMini();
        this.h5AppId = appId.getH5();
        this.officialAccountAppId = appId.getOfficial_account();
        this.slotCardAppId = appId.getSlot_card();
        this.scanCodeAppId = appId.getScan_code();
    }
    String getAppId(WXPayGenreEnum wxPayGenreEnum) {
        Integer value = wxPayGenreEnum.getValue();
        switch (value) {
            case 0:
                return appAppId;
            case 1:
                return miniAppId;
            case 2:
                return h5AppId;
            case 3:
                return officialAccountAppId;
            case 4:
                return slotCardAppId;
            case 5:
                return scanCodeAppId;
            default:
                return null;
        }
    }
}
