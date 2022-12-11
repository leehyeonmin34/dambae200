package com.dambae200.dambae200.domain.user.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UserUpdateRequest{
    @NotBlank
    private String nickname;
}