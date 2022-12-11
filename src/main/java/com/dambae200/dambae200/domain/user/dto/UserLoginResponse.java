package com.dambae200.dambae200.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {

    private UserGetResponse userInfo;
    private String accessToken;

}