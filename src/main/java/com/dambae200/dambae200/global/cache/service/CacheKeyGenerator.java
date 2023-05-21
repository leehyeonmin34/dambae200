package com.dambae200.dambae200.global.cache.service;

import com.dambae200.dambae200.global.cache.config.CacheType;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

public class CacheKeyGenerator {

    public static <K> String getCacheKey(CacheType cacheType, K key){
        return cacheType.getCacheName() + "::" + key;
    }
}
