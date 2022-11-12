package com.dambae200.dambae200.domain.user.service;

import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.domain.user.dto.UserDto;
import org.springframework.http.ResponseEntity;


public interface UserLoginService {
    public UserDto.GetResponse authenticate(String email, String pw);
}
