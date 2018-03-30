package com.github.catalpaflat.pay.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @author CatalpaFlat
 */
public enum WXPayGenreEnum {
    APP("app", 0), MINI("mini", 1), H5("h5", 2), OFFICIAL_ACCOUNT("official_accounts", 3),
    SLOT_CARD("slot_card", 4), SCAN_CODE("scan_code", 5);

    @Getter
    @Setter
    private String genre;
    @Getter
    @Setter
    private Integer value;

    WXPayGenreEnum(String genre, Integer value) {
        this.genre = genre;
        this.value = value;
    }
}
