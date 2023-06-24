package com.dambae200.dambae200.global.cache.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

@Getter
@RequiredArgsConstructor
public enum RedisServerType {
    SESSION("session", "sessionRedisTemplate"),
    CACHE("cache", "cacheRedisTemplate")
    ;

    private final String desc;
    private final String redisTemplateBeanName;

}
