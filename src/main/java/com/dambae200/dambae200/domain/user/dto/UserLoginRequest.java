package com.dambae200.dambae200.domain.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class UserLoginRequest {

    @ApiModelProperty(value="사용자 이메일", required = true)
    @Pattern(regexp = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$", message = "잘못된 이메일 형식입니다.")
    private String email;

    @ApiModelProperty(value="사용자 비밀번호", required = true)
    @Pattern(regexp = "^[A-Za-z0-9]{6,20}$",message = "비밀번호는 영문과 숫자를 포함해 6자 ~ 20자여야 합니다.")
    private String pw;
}