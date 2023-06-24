package com.dambae200.dambae200.global.cache.service;

import com.dambae200.dambae200.global.cache.config.CacheType;
import com.dambae200.dambae200.global.cache.config.RedisServerType;
import com.dambae200.dambae200.global.error.exception.UnhandledServerException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class RedisTemplateFinder {

//    @Qualifier("sessionRedisTemplate")
//    private final RedisTemplate sessionRedisTemplate;
//
//    @Qualifier("cacheRedisTemplate")
//    private final RedisTemplate cacheRedisTemplate;

    private final ApplicationContext ac;

    private final Map<RedisServerType, RedisTemplate> redisTemplateMap = new HashMap<>();

    public RedisTemplateFinder(ApplicationContext ac) {
        this.ac = ac;
        Arrays.stream(RedisServerType.values()).forEach(serverType -> {
            String redisTemplateBeanName = serverType.getRedisTemplateBeanName();
            RedisTemplate redisTemplate = ac.getBean(redisTemplateBeanName, RedisTemplate.class);
            if (redisTemplate == null)
                throw new UnhandledServerException(String.format("redisTemplate bean name: %s에 해당하는 redisTemplate이 없습니다. " +
                        "RedisServerType의 redisTemplateBeanName 값이나, redisTemplate들의 bean 이름을 확인해주세요", redisTemplateBeanName));
            redisTemplateMap.put(serverType, redisTemplate);
        });
    }

    public RedisTemplate findOf(CacheType cacheType){
        return redisTemplateMap.get(cacheType.getRedisServerType());
    }

}
