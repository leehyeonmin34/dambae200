package com.dambae200.dambae200.domain.cigarette.service;

import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;
import com.dambae200.dambae200.domain.cigarette.dto.CigaretteGetListResponse;
import com.dambae200.dambae200.domain.cigarette.dto.CigaretteGetResponse;
import com.dambae200.dambae200.domain.cigarette.repository.CigaretteRepository;
import com.dambae200.dambae200.global.cache.config.CacheEnvOld;
import com.dambae200.dambae200.global.cache.config.CacheType;
import com.dambae200.dambae200.global.cache.service.CacheModule;
import com.dambae200.dambae200.global.cache.service.CacheableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CigaretteFindService {

    private final CigaretteRepository cigaretteRepository;
    private final CacheableRepository cigaretteCacheableRepository;
    private final CacheModule cacheModule;

    @Transactional(readOnly = true)
    public CigaretteGetListResponse findAllCigarettes() {
        // 200여개의 모든 담배의 일반 정보를 각각 조회하지 않고
        // 캐시키 0L에 저장되어있는 1개의 목록 정보를 불러온다.
        return cacheModule.getCacheOrLoad(CacheType.CIGARETTE, Long.valueOf(0L)
                , this::loadCigarettes);
    }

    private CigaretteGetListResponse loadCigarettes(Long _id){
        List<CigaretteGetResponse> cigars = cigaretteRepository.findAll()
                .stream().map(CigaretteGetResponse::new)
                .collect(Collectors.toList());

        // 각각의 담배를 각각의 식별자대로 저장도 해줌
        cacheModule.putAllPipelined(CacheType.CIGARETTE, cigars, CigaretteGetResponse::getId);
        return new CigaretteGetListResponse(cigars);
    }

}
