package com.dambae200.dambae200.domain.access.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AccessGetStoreListResponse {
    private List<AccessGetStoreResponse> accesses;
    private int total;
}