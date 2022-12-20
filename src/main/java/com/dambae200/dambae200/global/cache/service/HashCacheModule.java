package com.dambae200.dambae200.global.cache.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    // cacheName, key, hashKey에 해당하는 캐시를 먼저 조회한 뒤, 캐시에 없으면 다른 저장소 조회(loadFunction 수행)
    // map의 entry에 대한 look-aside (단건)
    public <K, HK, V> V getCacheOrLoad(String cacheName, K key, HK hashKey, Function<HK, V> dbLoadFunctionByHashKey) {

        // 캐시가 있다면 바로 리턴
        if (ops.hasKey(getCacheKey(cacheName, key), hashKey)) {
            V cached = get(cacheName, key, hashKey);
            log.info(String.format("캐시 사용 - cachaName: %s, key: %s, hashKey: %s", cacheName, key.toString(), hashKey.toString()));
            return cached;
        }

        // 캐시가 없을 땐 다른 저장소 조회, 캐시에 저장
        V loaded = dbLoadFunctionByHashKey.apply(hashKey);
        put(cacheName, key, hashKey, loaded);
        return loaded;
    }

    // cacheName, key에 해당하는 캐시들을 먼저 조회한 뒤, 캐시에 없으면 다른 저장소 조회(loadFunction 수행)
    // key에 해당하는 map 전체에 대한 look-aside
    public <K, HK, V> Map<HK, V> getAllCacheOrLoad(String cacheName, K key, Function<K, List<V>> dbLoadFunctionByKey, Function<V, HK> keyExtractor){
        Map<HK, V> cached = getAll(cacheName, key);

        // 캐시에 값 있을 때 바로 리턴
        if (!cached.isEmpty()) {
            log.info(String.format("캐시 사용 - cachaName: %s, key: %s", cacheName, key.toString()));
            return cached;
        }

        // 캐시에 값 없을 땐 다른 저장소 조회, 캐시에 저장
        List<V> loaded = dbLoadFunctionByKey.apply(key);
        log.info("DB에서 불러옴" + loaded);
        putAll(cacheName, key, loaded, keyExtractor);

        // map으로 변환 후 리턴
        return loaded.stream()
                .collect(Collectors.toUnmodifiableMap(keyExtractor, item -> item));
    }

    // cacheName, key, hashKeys에 해당하는 캐시들을 먼저 조회한 뒤, 캐시에 없으면 다른 저장소 조회(loadFunction 수행)
    // map entry에 대한 look-aside (복수건)
    public <K, HK, V> Map<HK, V> getAllCacheOrLoadByHashKeys(String cacheName
            , K key
            , List<HK> hashKeys
            , Function<List<HK>
            , List<V>> dbLoadFunctionByHashKey
            , Function<V, HK> keyExtractor){
        String cacheKey = getCacheKey(cacheName, key);

        List<V> result = new ArrayList<>();
        List<HK> notCachedHashKeys = new ArrayList<>();

        hashKeys.forEach(hashKey -> {
            V cached = (V)ops.get(cacheKey, hashKey);
            if (cached != null) result.add(cached);
            else notCachedHashKeys.add(hashKey);
        });

        // 캐시에 없는 값은 DB 조회, 캐시에 저장 및 결과 병합
        if(!notCachedHashKeys.isEmpty()) {
            List<V> loaded = dbLoadFunctionByHashKey.apply(notCachedHashKeys);
            putAll(cacheName, key, loaded, keyExtractor);
            result.addAll(loaded);
        }

        // map으로 변환 후 리턴
        return result.stream()
                .collect(Collectors.toUnmodifiableMap(keyExtractor, item -> item));
    }

    // 캐시 조회 (단건)
    public <K, HK, V> V get(String cacheName, K key, HK hashKey){
        String cacheKey = getCacheKey(cacheName, key);
        return (V)ops.get(cacheKey, hashKey);
    }

    // 캐시 조회 (key에 대한 map 전체 조회)
    public <K, HK, V> Map<HK, V> getAll(String cacheName, K key){
        String cacheKey = getCacheKey(cacheName, key);
        return ops.entries(cacheKey);
    }



    // 캐시, DB에 모두 저장 (단건)
    public <K, HK, V> V writeThrough(String cacheName, K key, V value, Function<V, HK> keyExtractor,  UnaryOperator<V> dbWriteFunction){
        V saved = dbWriteFunction.apply(value);
        HK hashKey = keyExtractor.apply(saved);
        put(cacheName, key, hashKey, saved);
        return saved;
    }

    // 캐시, DB에 모두 저장 (복수건)
    public <K, HK, V> List<V> writeAllThrough(String cacheName, K key, List<V> values, Function<V, HK> keyExtractor, UnaryOperator<List<V>> dbWriteFunction){
        List<V> saved = dbWriteFunction.apply(values);
        putAll(cacheName, key, values, keyExtractor);
        return saved;
    }





    // 캐시 내용을 DB에 적용 후 캐시 삭제 (단건)
    public <K, HK, V> V flush(String cacheName, K key, HK hashKey, UnaryOperator<V> dbWriteFunction){
        V cached = get(cacheName, key, hashKey);
        return dbWriteFunction.apply(cached);
    }

    // 캐시 내용을 DB에 적용 후 캐시 삭제 (복수건)
    public <K, HK, V> List<V> flushAll(String cacheName, K key, UnaryOperator<List<V>> dbWriteFunction){
        List<V> cached = ((Map<HK, V>)getAll(cacheName, key))
                .values().stream().collect(Collectors.toList());
        List<V> saved = dbWriteFunction.apply(cached);
        evictAll(cacheName, key);
        return saved;
    }



    // 캐시 입력 (단건)
    public <K, HK, V> void put(String cacheName, K key, HK hashKey, V value){
        String cacheKey = getCacheKey(cacheName, key);
        log.info("캐시 저장" + value.toString());
        ops.put(cacheKey, hashKey, value);
    }

    // 캐시 입력 (복수건)
    public <K, HK, V> void putAll(String cacheName, K key, List<V> values, Function<V, HK> keyExtractor){
        String cacheKey = getCacheKey(cacheName, key);
        Map<HK, V> map = values.stream().collect(Collectors.toUnmodifiableMap(keyExtractor, item -> item));
        ops.putAll(cacheKey, map);
    }




    // 캐시 삭제 (단건)
    public <K, HK> void evict(String cacheName, K key, HK hashKey){
        String cacheKey = getCacheKey(cacheName, key);
        ops.delete(cacheKey, hashKey);
    }

    // 캐시 삭제 (복수건)
    public <K, HK> void evictAllByHashKeys(String cacheName, K key, List<HK> hashKeys){
        String cacheKey = getCacheKey(cacheName, key);
        for(HK hashKey: hashKeys)
            ops.delete(cacheKey, hashKey);
    }

    // 캐시 삭제 (key에 해당하는 map 전체)
    public <K, HK> void evictAll(String cacheName, K key){
        String cacheKey = getCacheKey(cacheName, key);
        Set<HK> hashKeys = ops.entries(cacheKey).keySet();
        for(HK hashKey: hashKeys)
            ops.delete(cacheKey, hashKey);
    }




    // 캐시, DB 모두 삭제 (단건)
    public <K, HK> void deleteThrough(String cacheName, K key, HK hashKey, Consumer<HK> dbDeleteFunction){
        dbDeleteFunction.accept(hashKey);
        evict(cacheName, key, hashKey);
    }

    // 캐시, DB 모두 삭제 (key에 해당하는 map 전체)
    public <K, HK> void deleteAllThrough(String cacheName, K key, Consumer<K> dbDeleteFunction){
        dbDeleteFunction.accept(key);
        evictAll(cacheName, key);
    }

    // 캐시, DB 모두 삭제 (복수건)
    public <K, HK> void deleteAllThroughByHashKeys(String cacheName, K key, List<HK> hashKeys, Consumer<List<HK>> dbDeleteFunction){
        dbDeleteFunction.accept(hashKeys);
        evictAllByHashKeys(cacheName, key, hashKeys);
    }

    private <K> String getCacheKey(String cacheName, K key){
        return cacheName + ":" + key;
    }


}
