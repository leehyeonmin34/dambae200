package com.dambae200.dambae200.global.cache.config;

import com.dambae200.dambae200.domain.access.domain.Access;
import com.dambae200.dambae200.domain.access.repository.AccessRepository;
import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;
import com.dambae200.dambae200.domain.cigarette.repository.CigaretteRepository;
import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import com.dambae200.dambae200.domain.cigaretteOnList.repository.CigaretteOnListRepository;
import com.dambae200.dambae200.global.cache.service.CacheModule;
import com.dambae200.dambae200.global.cache.service.CacheableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CacheableRepositoryBeans {

    final private AccessRepository accessRepository;
    final private CigaretteRepository cigaretteRepository;

    final private CacheModule cacheModule;

    @Bean
    public CacheableRepository<Long, Access, AccessRepository> accessCacheableRepository(){
        return new CacheableRepository<>(CacheEnv.ACCESS, accessRepository, Access::getId, cacheModule);
    }

    @Bean
    public CacheableRepository<Long, Cigarette, CigaretteRepository> cigaretteCacheableRepository(){
        return new CacheableRepository<>(CacheEnv.CIGARETTE, cigaretteRepository, Cigarette::getId, cacheModule);
    }

}
