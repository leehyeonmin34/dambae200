package com.dambae200.dambae200.domain.access.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter

public class AccessGetStoreListResponse {
    private List<AccessGetStoreResponse> accesses;
    private int total;

    public AccessGetStoreListResponse(List<AccessGetStoreResponse> list){
        this.accesses = list;
        this.total = list.size();
    }

}