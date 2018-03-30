package com.github.catalpaflat.pay.model.wx;

import lombok.Getter;
import lombok.Setter;

/**
 * @author CatalpaFlat
 */
@Getter
@Setter
public class CFPayWXAppIdTO {
    private String app;
    private String mini;
    private String h5;
    private String official_account;
    private String slot_card;
    private String scan_code;
}
