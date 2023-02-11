package com.dambae200.dambae200.domain.sessionInfo.service;


import com.dambae200.dambae200.domain.access.exception.AccessNotAllowedException;
import com.dambae200.dambae200.domain.sessionInfo.domain.SessionInfo;
import com.dambae200.dambae200.domain.sessionInfo.exception.AccessedExpiredSessionTokenException;
import com.dambae200.dambae200.domain.sessionInfo.exception.SessionInfoNotExistsException;
import com.dambae200.dambae200.domain.sessionInfo.repository.SessionInfoRepository;
import com.dambae200.dambae200.domain.user.dto.UserGetResponse;
import com.dambae200.dambae200.global.cache.service.CacheModule;
import com.dambae200.dambae200.global.cache.config.CacheEnv;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;


@Primary
@Service
@Transactional
@RequiredArgsConstructor
public class SessionServiceImplCacheModule implements SessionService {

    final private SessionInfoRepository sessionInfoRepository;
    final private CacheModule cacheModule;
    final private RedisCacheManager cacheManager;

    @Override
    public boolean existsByToken(final String accessToken){
        try {
            getSessionElseThrow(accessToken);
            return true;
        } catch (SessionInfoNotExistsException e) {
            return false;
        }
    }

    @Override
    public SessionInfo getSessionElseThrow(final String accessToken){

        // 캐시, DB 조회
        SessionInfo sessionInfo = cacheModule.getCacheOrLoad(CacheEnv.SESSION_INFO,
                accessToken,
                key -> sessionInfoRepository.findById(key)
                        .orElseThrow(SessionInfoNotExistsException::new)
        );

        // 만료되면 삭제 후 예외 발생
        removeIfExpired(sessionInfo);

        // 리턴
          return sessionInfo;
    }


    private void removeIfExpired(final SessionInfo sessionInfo){
        try {
            sessionInfo.checkExpiration();
        } catch (AccessedExpiredSessionTokenException e){
            cacheModule.deleteThrough(CacheEnv.SESSION_INFO, sessionInfo.getAccessToken(), sessionInfoRepository::deleteById);
            throw new SessionInfoNotExistsException();
        }
    }

    // 세션 키 등록
    @Override
    public SessionInfo registerSession(final UserGetResponse user, final String userAgent) {

        // 세션정보 생성
        String accessToken = UUID.randomUUID().toString();
        SessionInfo sessionInfo = new SessionInfo.Builder(accessToken, user.getId())
                .userAgent(userAgent)
                .expirationTime(LocalDateTime.now().plusDays(30))
                .build();

        // DB, 캐시에 저장
        SessionInfo saved = cacheModule.writeThrough(CacheEnv.SESSION_INFO, accessToken, sessionInfo, sessionInfoRepository::save);

        return saved;
    }


    // 세션 키 삭제
    @Override
    public void removeSession(final String accessToken) {
        if (accessToken != null)
            cacheModule.deleteThrough(CacheEnv.SESSION_INFO, accessToken, this::deleteIfExists);
    }


    private void deleteIfExists(final String accessToken){
        if(sessionInfoRepository.existsById(accessToken))
            sessionInfoRepository.deleteById(accessToken);
    }

}
