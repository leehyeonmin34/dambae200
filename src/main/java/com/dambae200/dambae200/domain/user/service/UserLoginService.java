package com.dambae200.dambae200.domain.user.service;


import com.dambae200.dambae200.domain.user.dto.UserGetResponse;

public interface UserLoginService {
    UserGetResponse authenticate(final String email, final String pw);
}
