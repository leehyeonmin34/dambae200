package com.dambae200.dambae200.domain.access.dto;

import com.dambae200.dambae200.domain.access.domain.Access;
import com.dambae200.dambae200.domain.user.domain.User;
import lombok.Getter;

@Getter
public class AccessGetUserResponse {
    private Long id;
    private Long userId;
    private String userNickname;
    private String userEmail;
    private String accessTypeCode;

    public AccessGetUserResponse(Access access) {
        User user = access.getUser();

        this.id = access.getId();
        this.accessTypeCode = access.getAccessType().getCode();
        this.userId = user.getId();
        this.userNickname = user.getNickname();
        this.userEmail = user.getEmail();
    }
}