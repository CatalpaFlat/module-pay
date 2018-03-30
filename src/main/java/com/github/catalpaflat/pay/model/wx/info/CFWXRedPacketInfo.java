package com.github.catalpaflat.pay.model.wx.info;

import com.github.catalpaflat.pay.enums.WXRebPacketEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author CatalpaFlat
 */
@Getter
@Setter
public class CFWXRedPacketInfo {
    private String openid;
    private String sendName;
    private String orderNum;
    private Integer totalAmount;
    private Integer totalNum;
    private String wishing;
    private String clientIp;
    private String amtType;
    private String actName;
    private String remark;
    private WXRebPacketEnum wxRebPacketEnum;
}
