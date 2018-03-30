package com.github.catalpaflat.pay.model.wx.info;

import com.github.catalpaflat.pay.enums.WXPayGenreEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author CatalpaFlat
 */
@Getter
@Setter
public class CFWxPayInfo {
    private String totalFee;
    private String openid;
    private String terminalIp;
    private String body;
    private String orderNum;
    private String goodsTag;
    private String tradeType;
    private String authCode;
    private WXPayGenreEnum wxPayGenreEnum;

    public CFWxPayInfo(String totalFee, String openid, String terminalIp, String body, String orderNum,
                       String tradeType, WXPayGenreEnum wxPayGenreEnum) {
        this.totalFee = totalFee;
        this.openid = openid;
        this.terminalIp = terminalIp;
        this.body = body;
        this.orderNum = orderNum;
        this.tradeType = tradeType;
        this.wxPayGenreEnum = wxPayGenreEnum;
    }

    public CFWxPayInfo setAuthCode(String authCode) {
        this.authCode = authCode;
        return this;
    }

    public CFWxPayInfo setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
        return this;
    }
}
