package com.dambae200.dambae200.domain.user.dto;

import com.dambae200.dambae200.domain.user.domain.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
public class UserGetListResponse{
    private List<UserGetResponse> users = new ArrayList<>();
    private int total = 0;

    public UserGetListResponse(List<User> users){
        this.users = users.stream()
                .map(UserGetResponse::new)
                .collect(Collectors.toList());
        this.total = users.size();
    }

}