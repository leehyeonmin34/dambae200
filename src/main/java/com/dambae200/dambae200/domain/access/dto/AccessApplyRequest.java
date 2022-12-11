package com.dambae200.dambae200.domain.access.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class AccessApplyRequest{
    @NotNull(message = "storeId가 누락되었습니다.")
    private Long storeId;
    @NotNull(message = "userId가 누락되었습니다.")
    private Long userId;
}