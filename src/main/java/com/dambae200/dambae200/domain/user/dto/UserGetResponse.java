package com.dambae200.dambae200.domain.user.dto;

import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.global.common.dto.BaseDto;
import lombok.Getter;

@Getter
public class UserGetResponse extends BaseDto {
    private String email;
    private String nickname;

    public UserGetResponse(User user){
        id = user.getId();
        createdAt = user.getCreatedAt();
        updatedAt = user.getUpdatedAt();
        email = user.getEmail();
        nickname = user.getNickname();
    }

}