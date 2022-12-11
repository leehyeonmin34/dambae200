package com.dambae200.dambae200.domain.cigaretteOnList.dto;

import lombok.Getter;

@Getter
public class CigaretteOnListAddRequest {
    private Long storeId;
    private Long cigaretteId;
    private String customizedName;
}