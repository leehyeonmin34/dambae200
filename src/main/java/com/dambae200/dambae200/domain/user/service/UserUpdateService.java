package com.dambae200.dambae200.domain.user.service;

import com.dambae200.dambae200.domain.access.repository.AccessRepository;
import com.dambae200.dambae200.domain.notification.repository.NotificationRepository;
import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.domain.user.dto.UserDto;
import com.dambae200.dambae200.domain.user.exception.EmailDuplicationException;
import com.dambae200.dambae200.domain.user.exception.LoginInputInvalidException;
import com.dambae200.dambae200.domain.user.exception.NicknameDuplicationException;
import com.dambae200.dambae200.domain.user.repository.UserRepository;
import com.dambae200.dambae200.global.common.DeleteResponse;
import com.dambae200.dambae200.global.common.RepoUtils;
import com.dambae200.dambae200.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUpdateService {

    final UserRepository userRepository;

    final AccessRepository accessRepository;

    final NotificationRepository notificationRepository;

    final RepoUtils repoUtils;

    public UserDto.GetResponse addUser(UserDto.AddRequest request){

        validateEmail(request.getEmail());
        validateNickname(request.getNickname());

        User user = User.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .pw(request.getPw())
                .build();
        User saved = userRepository.save(user);
        return new UserDto.GetResponse(saved);
    }


    public UserDto.GetResponse updateUser(Long id, UserDto.UpdateRequest request) throws EntityNotFoundException, NicknameDuplicationException {
        validateNickname(request.getNickname());
        User user = repoUtils.getOneElseThrowException(userRepository, id);
        user.changeNickname(request.getNickname());
        User saved = userRepository.save(user);
        return new UserDto.GetResponse(saved);
    }

    public UserDto.GetResponse changeUserPassword(Long id, UserDto.ChangePasswordRequest request) throws EntityNotFoundException, LoginInputInvalidException {
        User user = repoUtils.getOneElseThrowException(userRepository, id);
        user.changePw(request.getOldPw(), request.getNewPw());
        User saved = userRepository.save(user);
        return new UserDto.GetResponse(saved);
    }

    public DeleteResponse deleteUser(Long id, String pw) throws EntityNotFoundException, LoginInputInvalidException {

        // 인증
        User user = repoUtils.getOneElseThrowException(userRepository, id);
        user.authenticate(pw);

        // 연관 데이터 삭제
        accessRepository.deleteAllByUserId(id);
        notificationRepository.deleteAllByUserId(id);

        // 삭제
        userRepository.deleteById(id);
        return new DeleteResponse("user", id);
    }


    private void validateEmail(String email){
        if (userRepository.existsByEmail(email))
            throw new EmailDuplicationException();
    }

    private void validateNickname(String nickname){
        if (userRepository.existsByNickname(nickname))
            throw new NicknameDuplicationException();
    }

}


