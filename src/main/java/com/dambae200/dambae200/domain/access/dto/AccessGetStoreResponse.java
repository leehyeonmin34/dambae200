package com.dambae200.dambae200.domain.access.dto;

import com.dambae200.dambae200.domain.access.domain.Access;
import com.dambae200.dambae200.domain.store.domain.Store;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AccessGetStoreResponse{
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    String accessTypeCode;
    private String storeName;
    private String brandCode;
    private Long storeId;
    private boolean applicatorExists;

    @Builder
    public AccessGetStoreResponse(Access access, boolean applicatorExists){
        Store store = access.getStore();
        id = access.getId();
        createdAt = store.getCreatedAt();
        updatedAt = store.getUpdatedAt();
        storeName = store.getName();
        brandCode = store.getBrand().getCode();
        accessTypeCode = access.getAccessType().getCode();
        storeId = store.getId();
        this.applicatorExists = applicatorExists;
    }
}
