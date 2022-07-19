package com.dambae200.dambae200.domain.store.domain;

import com.dambae200.dambae200.domain.store.exception.InvalidStoreBrandCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum StoreBrand {
    CU("씨유", "SB01"),
    GS25("GS25", "SB02"),
    EMART24("이마트24", "SB03"),
    SEVEN_ELEVEN("세븐일레븐", "SB04"),
    MINISTOP("미니스톱", "SB05"),
    STORYWAY("스토리웨이", "SB06"),
    WITH_ME("위드미", "SB07"),
    ECT("기타", "SB99")
    ;

    private String desc;

    private String code;

    StoreBrand(String desc, String code){
        this.desc = desc;
        this.code = code;
    }

    public static StoreBrand ofCode(String code){
        if (code == null) return ECT;
        return Arrays.stream(StoreBrand.values())
                .filter(item -> item.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new InvalidStoreBrandCode(code));
    }

}
