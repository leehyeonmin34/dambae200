package com.dambae200.dambae200.domain.store.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class StoreUpdateRequest{

    @NotBlank(message = "매장 이름을 입력해주세요")
    private String name;

    private String storeBrandCode;

    @NotNull
    private Long userId;
}