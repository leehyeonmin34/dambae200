package com.dambae200.dambae200.global.cache.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Component
@Slf4j
public class HashCacheModule {

    final private RedisTemplate redisTemplate;
    final private HashOperations ops;

    public HashCacheModule(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.ops = redisTemplate.opsForHash();
    }

    //    cacheName, key에 해당하는 캐시들을 먼저 조회한 뒤, 캐시에 없으면 다른 저장소 조회(loadFunction 수행)
    public <K, HK, V> V getCacheOrLoad(String cacheName, K key, HK hashKey, Function<HK, V> dbLoadFunction) {

        // 캐시가 있다면 바로 리턴
        if (ops.hasKey(getCacheKey(cacheName, key), hashKey)) {
            V cached = get(cacheName, key, hashKey);
            log.info(String.format("캐시 사용 - cachaName: %s, key: %s, hashKey: %s", cacheName, key.toString(), hashKey.toString()));
            return cached;
        }

        // 캐시가 없을 땐 다른 저장소 조회, 캐시에 저장
        V loaded = dbLoadFunction.apply(hashKey);
        put(cacheName, key, hashKey, loaded);
        return loaded;
    }

    public <K, HK, V> Map<HK, V> getAllCacheOrLoad(String cacheName, K key, Function<K, List<V>> dbLoadFunction, Function<V, HK> keyExtractor){
        Map<HK, V> cached = getAll(cacheName, key);

        // 캐시에 값 있을 때 바로 리턴
        if (!cached.isEmpty()) {
            log.info(String.format("캐시 사용 - cachaName: %s, key: %s", cacheName, key.toString()));
            return cached;
        }

        // 캐시에 값 없을 땐 다른 저장소 조회, 캐시에 저장
        List<V> loaded = dbLoadFunction.apply(key);
        log.info("DB에서 불러옴" + loaded);
        putAll(cacheName, key, loaded, keyExtractor);

        // map으로 변환 후 리턴
        return loaded.stream()
                .collect(Collectors.toUnmodifiableMap(keyExtractor, item -> item));
    }

    public <K, HK, V> V get(String cacheName, K key, HK hashKey){
        String cacheKey = getCacheKey(cacheName, key);
        return (V)ops.get(cacheKey, hashKey);
    }

    public <K, HK, V> Map<HK, V> getAll(String cacheName, K key){
        String cacheKey = getCacheKey(cacheName, key);
       return ops.entries(cacheKey);
    }

    // 캐시, DB에 모두 저장
    public <K, HK, V> V writeThrough(String cacheName, K key, HK hashKey, V value, UnaryOperator<V> dbWriteFunction){
        V saved = dbWriteFunction.apply(value);

        put(cacheName, key, hashKey, saved);
        return saved;
    }

    public <K, HK, V> List<V> writeAllThrough(String cacheName, K key, List<V> values, Function<V, HK> keyExtractor, UnaryOperator<List<V>> dbWriteFunction){
        List<V> saved = dbWriteFunction.apply(values);
        putAll(cacheName, key, values, keyExtractor);
        return saved;
    }

    public <K, HK, V> V flush(String cacheName, K key, HK hashKey, UnaryOperator<V> dbWriteFunction){
        V cached = get(cacheName, key, hashKey);
        return dbWriteFunction.apply(cached);
    }

    public <K, HK, V> List<V> flushAll(String cacheName, K key, UnaryOperator<List<V>> dbWriteFunction){
        List<V> cached = ((Map<HK, V>)getAll(cacheName, key))
                .values().stream().collect(Collectors.toList());
        List<V> saved = dbWriteFunction.apply(cached);
        evictAll(cacheName, key);
        return saved;
    }

    public <K, HK, V> void put(String cacheName, K key, HK hashKey, V value){
        String cacheKey = getCacheKey(cacheName, key);
        log.info("캐시 저장" + value.toString());
        ops.put(cacheKey, hashKey, value);
    }

    public <K, HK, V> void putAll(String cacheName, K key, List<V> values, Function<V, HK> keyExtractor){
        String cacheKey = getCacheKey(cacheName, key);
        Map<HK, V> map = values.stream().collect(Collectors.toUnmodifiableMap(keyExtractor, item -> item));
        ops.putAll(cacheKey, map);
    }

    public <K, HK> void evict(String cacheName, K key, HK hashKey){
        String cacheKey = getCacheKey(cacheName, key);
        ops.delete(cacheKey, hashKey);
    }

    public <K, HK> void evictAll(String cacheName, K key){
        String cacheKey = getCacheKey(cacheName, key);
        Set<HK> hashKeys = ops.entries(cacheKey).keySet();
        for(HK hashKey: hashKeys)
            ops.delete(cacheKey, hashKey);
    }


    // 캐시, DB 모두 삭제
    public <K, HK> void deleteThrough(String cacheName, K key, HK hashKey, Consumer<K> dbDeleteFunction){
        dbDeleteFunction.accept(key);
        evict(cacheName, key, hashKey);
    }

    private <K> String getCacheKey(String cacheName, K key){
        return cacheName + ":" + key;
    }


}
