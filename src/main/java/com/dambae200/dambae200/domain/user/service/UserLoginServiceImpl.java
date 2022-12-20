package com.dambae200.dambae200.domain.user.service;

import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.domain.user.dto.UserGetResponse;
import com.dambae200.dambae200.domain.user.exception.LoginInfoNotMatched;
import com.dambae200.dambae200.domain.user.repository.UserRepository;
import com.dambae200.dambae200.global.common.service.RepoUtils;
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
    public UserGetResponse authenticate(String email, String pw) throws LoginInfoNotMatched {
        User user = userRepository.findByEmail(email)
                .orElseThrow(LoginInfoNotMatched::new);
        user.authenticate(pw);
        return new UserGetResponse(user);
    }
}
