package com.github.catalpaflat.pay.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @author CatalpaFlat
 */
public enum  WXRebPacketEnum {
    ORDINARY("ordinary"),FISSION("fission");

    @Getter
    @Setter
    private String genre;

    WXRebPacketEnum(String genre) {
        this.genre = genre;
    }
}
