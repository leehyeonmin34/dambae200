package com.dambae200.dambae200.domain.store.service;

import com.dambae200.dambae200.domain.access.repository.AccessRepository;
import com.dambae200.dambae200.domain.store.domain.Store;
import com.dambae200.dambae200.domain.store.dto.StoreDto;
import com.dambae200.dambae200.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreFindService {

    final AccessRepository accessRepository;
    final StoreRepository storeRepository;

    public StoreDto.GetListResponse findAllByUser(Long userId){
        List<Store> stores = accessRepository.findAllByUserId(userId)
                .stream().map(item -> item.getStore()).collect(Collectors.toList());
        return new StoreDto.GetListResponse(stores);
    }

    // TODO 테스트 필요
    public StoreDto.GetListResponse findByNameLike(String name){
        List<Store> stores = storeRepository.findByNameLike("%"+ name+ "%");
        return new StoreDto.GetListResponse(stores);
    }

}
