package com.dambae200.dambae200.global.cache.service;

import com.dambae200.dambae200.global.cache.config.CacheType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisTemplateFinder {

    @Qualifier("sessionRedisTemplate")
    private final RedisTemplate sessionRedisTemplate;

    @Qualifier("cacheRedisTemplate")
    private final RedisTemplate cacheRedisTemplate;

    public RedisTemplateFinder(@Qualifier("sessionRedisTemplate") RedisTemplate sessionRedisTemplate,
                               @Qualifier("cacheRedisTemplate") RedisTemplate cacheRedisTemplate) {
        this.sessionRedisTemplate = sessionRedisTemplate;
        this.cacheRedisTemplate = cacheRedisTemplate;
    }


    public RedisTemplate findOf(CacheType cacheType){
        System.out.println(sessionRedisTemplate);
        System.out.println(cacheRedisTemplate);
        if (cacheType.equals(CacheType.SESSION_INFO))
            return sessionRedisTemplate;
        else return cacheRedisTemplate;
    }
}
