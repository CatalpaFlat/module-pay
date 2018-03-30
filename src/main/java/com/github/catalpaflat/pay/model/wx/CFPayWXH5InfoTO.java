package com.github.catalpaflat.pay.model.wx;

import lombok.Getter;
import lombok.Setter;

/**
 * @author CatalpaFlat
 */
public class CFPayWXH5InfoTO {

    @Getter
    @Setter
    private CFPayWXH5WapTO h5_info;

    public CFPayWXH5InfoTO(CFPayWXH5WapTO h5_info) {
        this.h5_info = h5_info;
    }
}
