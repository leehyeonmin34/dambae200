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

    private final ApplicationContext ac;
    private final Map<RedisServerType, RedisTemplate> redisTemplateMap = new HashMap<>();

    public RedisTemplateFinder(ApplicationContext ac) {
        this.ac = ac;

        // 모든 RedisServerType enum에 지정된 redisTemplateBeanName 값을 이용해 빈을 map에 저장해 놓는다.
        Arrays.stream(RedisServerType.values()).forEach(serverType -> {
            String redisTemplateBeanName = serverType.getRedisTemplateBeanName();
            RedisTemplate redisTemplate = ac.getBean(redisTemplateBeanName, RedisTemplate.class);

            // 해당 이름의 bean이 없으면 500 예외 발생
            if (redisTemplate == null)
                throw new UnhandledServerException(String.format("redisTemplate bean name: %s에 해당하는 redisTemplate이 없습니다. " +
                        "RedisServerType의 redisTemplateBeanName 값이나, redisTemplate들의 bean 이름을 확인해주세요", redisTemplateBeanName));

            redisTemplateMap.put(serverType, redisTemplate);
        });
    }

    // map에서 캐시타입에 맞는 RedisTemplate bean 찾아서 반환
    public RedisTemplate findOf(CacheType cacheType) {
        return redisTemplateMap.get(cacheType.getRedisServerType());
    }

}