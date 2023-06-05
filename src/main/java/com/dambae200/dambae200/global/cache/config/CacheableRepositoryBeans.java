package com.dambae200.dambae200.global.cache.config;

import com.dambae200.dambae200.domain.access.domain.Access;
import com.dambae200.dambae200.domain.access.dto.AccessGetResponse;
import com.dambae200.dambae200.domain.access.repository.AccessRepository;
import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;
import com.dambae200.dambae200.domain.cigarette.dto.CigaretteGetResponse;
import com.dambae200.dambae200.domain.cigarette.repository.CigaretteRepository;
import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import com.dambae200.dambae200.domain.cigaretteOnList.dto.CigaretteOnListGetResponse;
import com.dambae200.dambae200.domain.cigaretteOnList.repository.CigaretteOnListRepository;
import com.dambae200.dambae200.domain.sessionInfo.domain.SessionInfo;
import com.dambae200.dambae200.domain.sessionInfo.dto.SessionInfoDto;
import com.dambae200.dambae200.domain.sessionInfo.repository.SessionInfoRepository;
import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.domain.user.dto.UserGetResponse;
import com.dambae200.dambae200.domain.user.repository.UserRepository;
import com.dambae200.dambae200.global.cache.service.CacheModule;
import com.dambae200.dambae200.global.cache.service.CacheableRepository;
import com.dambae200.dambae200.global.cache.service.HashCacheModule;
import com.dambae200.dambae200.global.cache.service.HashCacheableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CacheableRepositoryBeans {

    final private AccessRepository accessRepository;
    final private CigaretteRepository cigaretteRepository;
    final private CigaretteOnListRepository cigaretteOnListRepository;
    final private UserRepository userRepository;
    final private SessionInfoRepository sessionInfoRepository;
    final private CacheModule cacheModule;
    final private HashCacheModule hashCacheModule;

    @Bean
    public CacheableRepository<Access, AccessGetResponse, Long, AccessRepository>  accessCacheableRepository(){
        return new CacheableRepository<>(CacheType.ACCESS, accessRepository, Access::getId, AccessGetResponse::getId, cacheModule, AccessGetResponse.class, Access.class);
    }

    @Bean
    public CacheableRepository<Cigarette, CigaretteGetResponse, Long, CigaretteRepository>  cigaretteCacheableRepository(){
        return new CacheableRepository<>(CacheType.CIGARETTE, cigaretteRepository, Cigarette::getId, CigaretteGetResponse::getId, cacheModule, CigaretteGetResponse.class, Cigarette.class);
    }

    @Bean
    public CacheableRepository<User, UserGetResponse, Long, UserRepository> userCacheableRepository(){
        return new CacheableRepository<>(CacheType.USER, userRepository, User::getId, UserGetResponse::getId, cacheModule, UserGetResponse.class, User.class);
    }

    @Bean
    public CacheableRepository<SessionInfo, SessionInfoDto, String, SessionInfoRepository> sessionInfoCacheableRepository(){
        return new CacheableRepository<>(CacheType.SESSION_INFO, sessionInfoRepository, SessionInfo::getAccessToken, SessionInfoDto::getAccessToken, cacheModule, SessionInfoDto.class, SessionInfo.class);
    }

    @Bean // key는 storeId, hashKey는 cigaretteOnListId로, 가게별 담배목록을 관리
    public HashCacheableRepository<CigaretteOnList, CigaretteOnListGetResponse, Long, Long, CigaretteOnListRepository> cigaretteOnListCacheableRepository(){
        return new HashCacheableRepository<>(CacheType.CIGARETTE_LIST, cigaretteOnListRepository, hashCacheModule, CigaretteOnListGetResponse::getId, CigaretteOnList::getId, CigaretteOnListGetResponse.class, CigaretteOnList.class);
    }

}
