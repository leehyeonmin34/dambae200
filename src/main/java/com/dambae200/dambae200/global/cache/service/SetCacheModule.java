package com.dambae200.dambae200.global.cache.service;
import static com.dambae200.dambae200.global.cache.service.CacheKeyGenerator.*;

import com.dambae200.dambae200.global.cache.config.CacheType;
import org.slf4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Component
public class SetCacheModule {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(SetCacheModule.class);
    final private RedisTemplate redisTemplate;
    final private SetOperations ops;

    public SetCacheModule(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.ops = redisTemplate.opsForSet();
    }

    public <K, V> List<V> getAllCacheOrLoad(CacheType cacheType, K key, Function<K, List<V>> dbLoadFunction){
        Set<V> cached = getAll(cacheType, key);

        // 캐시에 값 있을 때 바로 리턴
        if (!cached.isEmpty()) {
            log.info("캐시 사용" + cached);
            return cached.stream().collect(Collectors.toList());
        }

        // 캐시에 값 없을 땐 다른 저장소 조회, 캐시에 저장
        List<V> loaded = dbLoadFunction.apply(key);
        putAll(cacheType, key, loaded);

        return loaded;
    }

    public <K, V> Set<V> getAll(CacheType cacheType, K key){
        String cacheKey = getCacheKey(cacheType, key);
        return ops.members(cacheKey);
    }

    // 캐시, DB에 모두 저장
    public <K, V> V writeThrough(CacheType cacheType, K key, V value, UnaryOperator<V> dbWriteFunction){
        V saved = dbWriteFunction.apply(value);
        put(cacheType, key, saved);
        return saved;
    }

    public <K, HK, V> List<V> writeAllThrough(CacheType cacheType, K key, List<V> values, UnaryOperator<List<V>> dbWriteFunction){
        List<V> saved = dbWriteFunction.apply(values);
        putAll(cacheType, key, values);
        return saved;
    }

    public <K, V> List<V> flushAll(CacheType cacheType, K key, UnaryOperator<List<V>> dbWriteFunction){
        String cacheKey = getCacheKey(cacheType, key);

        List<V> cached = new ArrayList<>(getAll(cacheType, key));
        List<V> saved = dbWriteFunction.apply(cached);
        for(V value : cached)
            ops.remove(cacheKey, value);
        return saved;
    }

    public <K, V> void put(CacheType cacheType, K key, V value){
        String cacheKey = getCacheKey(cacheType, key);
        ops.add(cacheKey, value);
        redisTemplate.expire(key, cacheType.getTtlSecond(), TimeUnit.SECONDS);
    }

    public <K, V> void replaceAll(CacheType cacheType, K key, Collection<V> values){
        evictAll(cacheType, key);
        putAll(cacheType, key, values);
    }

    public <K, V> void putAll(CacheType cacheType, K key, Collection<V> values){

        String cacheKey = getCacheKey(cacheType, key);
        for(V value: values)
            ops.add(cacheKey, value);
        redisTemplate.expire(key, cacheType.getTtlSecond(), TimeUnit.SECONDS);
    }

    public <K, V> void evictAll(CacheType cacheType, K key){
        String cacheKey = getCacheKey(cacheType, key);
        redisTemplate.delete(cacheKey);
    }



}
