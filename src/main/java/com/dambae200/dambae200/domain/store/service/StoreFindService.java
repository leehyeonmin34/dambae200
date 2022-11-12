package com.dambae200.dambae200.domain.store.service;

import com.dambae200.dambae200.domain.access.domain.Access;
import com.dambae200.dambae200.domain.access.repository.AccessRepository;
import com.dambae200.dambae200.domain.store.domain.Store;
import com.dambae200.dambae200.domain.store.dto.StoreDto;
import com.dambae200.dambae200.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreFindService {

    final StoreRepository storeRepository;

    // TODO 테스트 필요
    public StoreDto.GetListResponse findByNameLike(String name){
        System.out.println(name);
        List<Store> stores = storeRepository.findByNameLike("%"+ name+ "%");
        return new StoreDto.GetListResponse(stores);
    }



}
