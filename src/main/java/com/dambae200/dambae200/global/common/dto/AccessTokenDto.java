package com.dambae200.dambae200.global.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AccessTokenDto {
    @ApiModelProperty(value="액세스 토큰", example = "6e84ffb6-2a46-4be4-a9b0-0a8d4747b426", required = true)
    private String accessToken;
}