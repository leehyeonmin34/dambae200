package com.dambae200.dambae200.domain.access.domain;

import com.dambae200.dambae200.domain.access.exception.InvalidAccessTypeCodeException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AccessType {
    INACCESSIBLE("접근 불가", "AT01"),
    WAITING("대기 중", "AT02"),
    ACCESSIBLE("접근 가능", "AT03"),
    ADMIN("관리자", "AT04")
    ;

    private String desc;
    private String code;

    public static AccessType ofCode(String code){
        if (code == null) return INACCESSIBLE;
        return Arrays.stream(AccessType.values())
                .filter(item -> item.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new InvalidAccessTypeCodeException(code));
    }

}
