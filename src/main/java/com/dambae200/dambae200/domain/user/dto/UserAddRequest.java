package com.dambae200.dambae200.domain.user.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Getter
public class UserAddRequest{
    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;
    @Pattern(regexp = "^[A-Za-z0-9]{6,20}$",message = "비밀번호는 영문과 숫자를 포함해 6자 ~ 20자여야 합니다.")
    private String pw;
    @NotBlank
    private String nickname;
}