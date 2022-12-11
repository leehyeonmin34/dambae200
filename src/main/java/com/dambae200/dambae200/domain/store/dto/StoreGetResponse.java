package com.dambae200.dambae200.domain.store.dto;

import com.dambae200.dambae200.domain.store.domain.Store;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class StoreGetResponse{

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String name;
    private String brandCode;

    @Builder
    public StoreGetResponse(Store store){
        id = store.getId();
        createdAt = store.getCreatedAt();
        updatedAt = store.getUpdatedAt();
        name = store.getName();
        brandCode = store.getBrand().getCode();
    }

}