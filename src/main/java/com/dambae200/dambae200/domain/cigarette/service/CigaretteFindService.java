package com.dambae200.dambae200.domain.cigarette.service;

import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;
import com.dambae200.dambae200.domain.cigarette.dto.CigaretteGetListResponse;
import com.dambae200.dambae200.domain.cigarette.repository.CigaretteRepository;
import com.dambae200.dambae200.global.cache.config.CacheEnvOld;
import com.dambae200.dambae200.global.cache.config.CacheType;
import com.dambae200.dambae200.global.cache.service.CacheModule;
import com.dambae200.dambae200.global.cache.service.CacheableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CigaretteFindService {

    private final CigaretteRepository cigaretteRepository;
    private final CacheableRepository<Long, Cigarette, CigaretteRepository> cigaretteCacheableRepository;
    private final CacheModule cacheModule;

    @Transactional(readOnly = true)
    public CigaretteGetListResponse findAllCigarettes() {
        // 전체 공식 담배 정보는 목록으로도 관리되고, 각 항목으로도 캐시에 올라간다
        final List<Cigarette> cigarettes = cacheModule.getCacheOrLoad(CacheType.CIGARETTE, 0L
                , (key) -> {
                    List<Cigarette> loaded = cigaretteRepository.findAll();
                    cigaretteCacheableRepository.writeAllThrough(loaded);
                    return loaded;
                });
        return new CigaretteGetListResponse(cigarettes);
    }

}
