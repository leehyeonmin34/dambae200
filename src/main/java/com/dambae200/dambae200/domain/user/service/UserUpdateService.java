package com.dambae200.dambae200.domain.user.service;

import com.dambae200.dambae200.domain.access.repository.AccessRepository;
import com.dambae200.dambae200.domain.notification.repository.NotificationRepository;
import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.domain.user.dto.UserAddRequest;
import com.dambae200.dambae200.domain.user.dto.UserChangePasswordRequest;
import com.dambae200.dambae200.domain.user.dto.UserGetResponse;
import com.dambae200.dambae200.domain.user.dto.UserUpdateRequest;
import com.dambae200.dambae200.domain.user.exception.DuplicatedEmailException;
import com.dambae200.dambae200.domain.user.exception.EmailNotFoundException;
import com.dambae200.dambae200.domain.user.exception.LoginInfoNotMatched;
import com.dambae200.dambae200.domain.user.exception.DuplicatedNicknameException;
import com.dambae200.dambae200.domain.user.repository.UserRepository;
import com.dambae200.dambae200.global.common.dto.DeleteResponse;
import com.dambae200.dambae200.global.common.service.RepoUtils;
import com.dambae200.dambae200.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserUpdateService {

    final UserRepository userRepository;

    final AccessRepository accessRepository;

    final NotificationRepository notificationRepository;

    final RepoUtils repoUtils;

    final UserTransaction userTransaction;

    public UserGetResponse addUser(final UserAddRequest request){

        validateEmail(request.getEmail());
        validateNickname(request.getNickname());

        User user = new User(request.getEmail(), request.getPw(), request.getNickname());
        User saved = userRepository.save(user);
        return new UserGetResponse(saved);
    }

    public UserGetResponse updateUser(final Long id, final UserUpdateRequest request){
        validateNickname(request.getNickname());
        User user = repoUtils.getOneElseThrowException(userRepository, id);
        user.changeNickname(request.getNickname());
        User saved = userRepository.save(user);
        return new UserGetResponse(saved);
    }

    public UserGetResponse changeUserPassword(final Long id, final UserChangePasswordRequest request){
        User user = repoUtils.getOneElseThrowException(userRepository, id);
        user.changePw(request.getOldPw(), request.getNewPw());
        User saved = userRepository.save(user);
        return new UserGetResponse(saved);
    }

    public void sendNewPwAndChangePw(final String email){
        User user = findByEmail(email);

        // Transaction 덕분에 비밀번호를 재지정한 뒤 알림 메일 전송이 실패해도, 비밀번호가 제대로 돌아옴
        String code = PasswordGenerator.generatePw();
        user.changePw(user.getPw(), code);
        userRepository.save(user);

        PasswordSender.send(code, email);
    }

    private User findByEmail(final String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> { throw new EmailNotFoundException(email);});
    }

    @Transactional
    public DeleteResponse deleteUser(final Long id, final String pw) throws EntityNotFoundException, LoginInfoNotMatched {

        // 인증
        User user = repoUtils.getOneElseThrowException(userRepository, id);
        user.authenticate(pw);

        // 연관 데이터 삭제
        userTransaction.deleteById(id);

        return new DeleteResponse("user", id);
    }


    @Transactional(readOnly = true)
    private void validateEmail(final String email){
        if (userRepository.existsByEmail(email))
            throw new DuplicatedEmailException();
    }

    @Transactional(readOnly = true)
    private void validateNickname(final String nickname){
        if (userRepository.existsByNickname(nickname))
            throw new DuplicatedNicknameException();
    }

}


