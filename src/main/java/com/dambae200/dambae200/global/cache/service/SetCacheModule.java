package com.dambae200.dambae200.global.cache.service;

import org.slf4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Component
public class SetCacheModule {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(SetCacheModule.class);
    final private RedisTemplate redisTemplate;

    public SetCacheModule(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public <K, V> List<V> getAllCacheOrLoad(String cacheName, K key, Function<K, List<V>> dbLoadFunction){
        SetOperations<String, V> ops = redisTemplate.opsForSet();
        Set<V> cached = getAll(cacheName, key);

        // 캐시에 값 있을 때 바로 리턴
        if (!cached.isEmpty()) {
            log.info("캐시 사용" + cached);
            return cached.stream().collect(Collectors.toList());
        }

        // 캐시에 값 없을 땐 다른 저장소 조회, 캐시에 저장
        List<V> loaded = dbLoadFunction.apply(key);
        putAll(cacheName, key, loaded);

        return loaded;
    }

    public <K, V> Set<V> getAll(String cacheName, K key){
        SetOperations<String, V> ops = redisTemplate.opsForSet();

        String cacheKey = getCacheKey(cacheName, key);
        return ops.members(cacheKey);
    }

    // 캐시, DB에 모두 저장
    public <K, V> V writeThrough(String cacheName, K key, V value, UnaryOperator<V> dbWriteFunction){
        V saved = dbWriteFunction.apply(value);
        put(cacheName, key, saved);
        return saved;
    }

    public <K, HK, V> List<V> writeAllThrough(String cacheName, K key, List<V> values, UnaryOperator<List<V>> dbWriteFunction){
        List<V> saved = dbWriteFunction.apply(values);
        putAll(cacheName, key, values);
        return saved;
    }

    public <K, V> List<V> flushAll(String cacheName, K key, UnaryOperator<List<V>> dbWriteFunction){
        SetOperations<String, V> ops = redisTemplate.opsForSet();
        String cacheKey = getCacheKey(cacheName, key);

        List<V> cached = new ArrayList<>(getAll(cacheName, key));
        List<V> saved = dbWriteFunction.apply(cached);
        for(V value : cached)
            ops.remove(cacheKey, value);
        return saved;
    }

    public <K, V> void put(String cacheName, K key, V value){
        SetOperations<String, V> ops = redisTemplate.opsForSet();

        String cacheKey = getCacheKey(cacheName, key);
        ops.add(cacheKey, value);
    }

    public <K, V> void replaceAll(String cacheName, K key, Collection<V> values){
        evictAll(cacheName, key);
        putAll(cacheName, key, values);
    }

    public <K, V> void putAll(String cacheName, K key, Collection<V> values){
        SetOperations<String, V> ops = redisTemplate.opsForSet();

        String cacheKey = getCacheKey(cacheName, key);
        for(V value: values)
            ops.add(cacheKey, value);
//        ops.add(cacheKey, values.toArray(new Object[0]));
    }

    public <K, V> void evictAll(String cacheName, K key){
        String cacheKey = getCacheKey(cacheName, key);
        redisTemplate.delete(cacheKey);
    }

    private <K> String getCacheKey(String cacheName, K key){
        return cacheName + ":" + key;
    }


}
