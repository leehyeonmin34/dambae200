package com.dambae200.dambae200.domain.user.service;

import com.dambae200.dambae200.domain.access.domain.Access;
import com.dambae200.dambae200.domain.access.repository.AccessRepository;
import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.domain.user.dto.UserDto;
import com.dambae200.dambae200.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserFindService {

    final AccessRepository accessRepository;

    final UserRepository userRepository;

    public UserDto.GetListResponse findAllByStoreId(Long storeId){
        List<User> users = accessRepository.findAllByStoreId(storeId)
                .stream().map(access -> access.getUser()).collect(Collectors.toList());
        return new UserDto.GetListResponse(users);
    }

    public Boolean existsByEmail(String email){
        if (userRepository.existsByEmail(email)) return true;
        else return false;
    }

    public Boolean existsByNickname(String nickName){
        if (userRepository.existsByNickname(nickName)) return true;
        else return false;
    }


}
