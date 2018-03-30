package com.github.catalpaflat.pay.model.wx;

import lombok.Getter;
import lombok.Setter;

/**
 * @author CatalpaFlat
 */
@Getter
@Setter
public class CFPayWXH5WapTO {

    private String type;

    private String wap_url;

    private String wap_name;

    public CFPayWXH5WapTO(String type, String wap_url, String wap_name) {
        this.type = type;
        this.wap_url = wap_url;
        this.wap_name = wap_name;
    }

    public CFPayWXH5WapTO() {
    }
}
