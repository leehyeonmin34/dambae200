package com.dambae200.dambae200.domain.cigaretteOnList.service;

import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import com.dambae200.dambae200.domain.cigaretteOnList.dto.CigaretteOnListGetListResponse;
import com.dambae200.dambae200.domain.cigaretteOnList.dto.CigaretteOnListGetResponse;
import com.dambae200.dambae200.domain.cigaretteOnList.repository.CigaretteOnListRepository;
import com.dambae200.dambae200.global.cache.config.CacheEnvOld;
import com.dambae200.dambae200.global.cache.config.CacheType;
import com.dambae200.dambae200.global.cache.service.HashCacheModule;
import com.dambae200.dambae200.global.cache.service.HashCacheableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CigaretteOnListFindService {

    private final CigaretteOnListRepository cigaretteOnListRepository;
    private final HashCacheModule hashCacheModule;
    private final HashCacheableRepository<CigaretteOnList, CigaretteOnListGetResponse, Long, Long, CigaretteOnListRepository> cigaretteOnListCacheableRepository;

    //리스트에 있는 담배 보여주기(진열순서)
    @Transactional(readOnly = true)
    public CigaretteOnListGetListResponse findAllByStoreIdOrderByDisplay(final Long storeId) {
        List<CigaretteOnListGetResponse> cigaretteOnLists = cigaretteOnListCacheableRepository
                .getAllCacheOrLoad(storeId, this::loadCigaretteOnListRepositoryByStoreId)
                .values().stream()
                .sorted(Comparator.comparingInt(CigaretteOnListGetResponse::getDisplayOrder))
                .collect(Collectors.toList());
        return new CigaretteOnListGetListResponse(cigaretteOnLists);
    }

    private List<CigaretteOnListGetResponse> loadCigaretteOnListRepositoryByStoreId(Long storeId){
        return cigaretteOnListRepository.findAllByStoreIdOrderByDisplay(storeId)
                .stream()
                .map(CigaretteOnListGetResponse::new)
                .collect(Collectors.toList());
    }

}
