package com.dambae200.dambae200.domain.user.service;

import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.domain.user.dto.UserDto;
import com.dambae200.dambae200.domain.user.exception.LoginInfoNotMatched;
import com.dambae200.dambae200.domain.user.repository.UserRepository;
import com.dambae200.dambae200.global.common.RepoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService{

    final private UserRepository userRepository;

    final private RepoUtils repoUtils;

    @Override
    @Transactional
    public UserDto.GetResponse authenticate(String email, String pw) throws LoginInfoNotMatched {
        User user = userRepository.findByEmail(email)
                .orElseThrow(LoginInfoNotMatched::new);
        user.authenticate(pw);
        return new UserDto.GetResponse(user);
    }

}
