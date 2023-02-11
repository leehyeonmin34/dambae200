package com.dambae200.dambae200.domain.store.service;

import com.dambae200.dambae200.domain.store.domain.Store;
import com.dambae200.dambae200.domain.store.dto.StoreGetListResponse;
import com.dambae200.dambae200.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreFindService {

    final StoreRepository storeRepository;

    public StoreGetListResponse findByNameLike(final String name){
        List<Store> stores = storeRepository.findByNameLike("%"+ name+ "%");
        return new StoreGetListResponse(stores);
    }



}
