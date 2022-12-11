package com.dambae200.dambae200.domain.access.dto;

import com.dambae200.dambae200.domain.access.domain.Access;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AccessGetUserListResponse {
    private List<AccessGetUserResponse> accesses = new ArrayList<>();
    private int total = 0;

    public AccessGetUserListResponse(List<Access> access){
        this.accesses = access.stream()
                .map(AccessGetUserResponse::new)
                .collect(Collectors.toList());
        this.total = accesses.size();
    }

}