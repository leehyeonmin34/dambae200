package com.dambae200.dambae200.global.cache;

import com.dambae200.dambae200.global.cache.config.CacheType;
import com.dambae200.dambae200.global.cache.service.CacheModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class TestCacheService {

    private final CacheModule cacheModule;

    public void put(int num){
        cacheModule.put(CacheType.TEST, num, num);
    }

    public void putFail(int num){
        cacheModule.put(CacheType.TEST, num, num);
        throw new RuntimeException();
    }

    public int putAndGetOrLoad(int num){
        cacheModule.put(CacheType.TEST, num, num);
        int cached = cacheModule.getCacheOrLoad(CacheType.TEST, num, (_id) -> num * 1000);

        if (cached == 0) throw new RuntimeException();
        else return cached;
    }

    public int putAndGetOrLoadFail(int num){
        cacheModule.put(CacheType.TEST, num, num);
        int cached = cacheModule.getCacheOrLoad(CacheType.TEST, num, (_id) -> num * 1000);
        throw new RuntimeException();
    }



}
