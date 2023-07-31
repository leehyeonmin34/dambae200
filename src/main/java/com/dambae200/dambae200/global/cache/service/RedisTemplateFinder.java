package com.dambae200.dambae200.global.cache.service;

import com.dambae200.dambae200.global.cache.config.CacheType;
import com.dambae200.dambae200.global.cache.config.RedisServerType;
import com.dambae200.dambae200.global.error.exception.UnhandledServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RedisTemplateFinder {

    // 빈 자동 주입으로 인해, key는 bean Name이다.
    private final Map<String, RedisTemplate> redisTemplateMap;


    public RedisTemplate findOf(CacheType cacheType) {

        // Redis서버 분리 없이 단일서버로만 캐시운용
        return redisTemplateMap.get(RedisServerType.CACHE.getRedisTemplateBeanName());

        // map에서 캐시타입에 맞는 RedisTemplate bean 찾아서 반환
//        return redisTemplateMap.get(cacheType.getRedisServerType().getRedisTemplateBeanName());
    }

}