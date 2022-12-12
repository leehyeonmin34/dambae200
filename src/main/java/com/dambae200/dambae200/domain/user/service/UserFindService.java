package com.dambae200.dambae200.domain.user.service;

import com.dambae200.dambae200.domain.access.repository.AccessRepository;
import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.domain.user.dto.UserGetListResponse;
import com.dambae200.dambae200.domain.user.dto.UserGetResponse;
import com.dambae200.dambae200.domain.user.repository.UserRepository;
import com.dambae200.dambae200.global.common.service.RepoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserFindService {

    final AccessRepository accessRepository;
    final UserRepository userRepository;
    final RepoUtils repoUtils;

    @Transactional(readOnly = true)
    public UserGetListResponse findAllByStoreId(Long storeId){
        List<User> users = accessRepository.findAllByStoreId(storeId)
                .stream().map(access -> access.getUser()).collect(Collectors.toList());
        return new UserGetListResponse(users);
    }

    @Transactional(readOnly = true)
    public UserGetResponse findById(Long id) {
        User user = repoUtils.getOneElseThrowException(userRepository, id);
        return new UserGetResponse(user);
    }

    @Transactional(readOnly = true)
    public Boolean existsByEmail(String email){
        if (userRepository.existsByEmail(email)) return true;
        else return false;
    }

    @Transactional(readOnly = true)
    public Boolean existsByNickname(String nickName){
        if (userRepository.existsByNickname(nickName)) return true;
        else return false;
    }


}
