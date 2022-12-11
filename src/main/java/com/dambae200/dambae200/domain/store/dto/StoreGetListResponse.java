package com.dambae200.dambae200.domain.store.dto;

import com.dambae200.dambae200.domain.store.domain.Store;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class StoreGetListResponse {

    private List<StoreGetResponse> stores;
    private int total;

    public StoreGetListResponse(List<Store> stores) {
        this.stores = stores.stream().map(StoreGetResponse::new)
                .collect(Collectors.toList());
        this.total = stores.size();
    }

}