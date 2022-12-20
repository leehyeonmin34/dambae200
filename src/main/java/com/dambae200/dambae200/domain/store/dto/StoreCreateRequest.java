package com.dambae200.dambae200.domain.store.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
public class StoreCreateRequest {

    @NotBlank(message = "매장 이름을 입력해주세요")
    private String name;

    private String storeBrandCode;

    @NotNull(message = "관리자 id가 누락되었습니다")
    private Long adminId;
}