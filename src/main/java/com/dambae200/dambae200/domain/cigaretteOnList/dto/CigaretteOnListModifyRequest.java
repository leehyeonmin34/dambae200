package com.dambae200.dambae200.domain.cigaretteOnList.dto;

import lombok.Getter;

@Getter
public class CigaretteOnListModifyRequest {
    Long id;
    Long requestUserId;
    Long storeId;
    String customizedName;
}
