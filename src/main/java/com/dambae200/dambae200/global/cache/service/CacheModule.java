package com.dambae200.dambae200.global.cache.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;


@Component
@Slf4j
public class CacheModule {

    final private RedisTemplate redisTemplate;
    final private RedisSerializer keySerializer;
    final private RedisSerializer valueSerializer;
    final private ValueOperations ops;


    public CacheModule(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.keySerializer = redisTemplate.getKeySerializer();
        this.valueSerializer = redisTemplate.getValueSerializer();
        this.ops = redisTemplate.opsForValue();
    }

    // cacheName, key에 해당하는 캐시들을 먼저 조회한 뒤, 캐시에 없으면 다른 저장소 조회(loadFunction 수행)
    // 단수 조회
    public <K, V> V getCacheOrLoad(String cacheName, K key, Function<K, V> dbLoadFunction) {

        V cached = (V) ops.get(key);

        // 캐시에 값 있을 때 바로 리턴
        if (cached != null) {
            log.info(String.format("캐시 사용 - cachaName: %s, key: %s", cacheName, key));
            return (V) cached;
        }

        // 캐시에 값 없을 땐 다른 저장소 조회, 캐시에 저장
        V loaded = dbLoadFunction.apply(key);
        ops.set(key, loaded);
        return loaded;
    }

    // 복수개의 Key들을 입력받아 조회
    public <K, V> List<V> getAllCacheOrLoadByKeys(String cacheName
            , List<K> keys
            , Function<List<K>
            , List<V>> dbLoadFunction){


        List<K> notCachedKeys = new ArrayList<>();
        List<V> result = new ArrayList<>();

        keys.forEach(key -> {
            String cacheKey = getCacheKey(cacheName, key);
            V cached = (V)ops.get(cacheKey);
            // 캐시에 값 있으면 결과에 추가, 없으면 따로 리스트에 적재
            if (cached != null) result.add(cached);
            else notCachedKeys.add(key);
        });

        // 캐시되지 않은 값들은 DB에서 불러옴
        List<V> loaded = dbLoadFunction.apply(notCachedKeys);
        result.addAll(loaded);

        return result;
    }

    // 캐시, DB에 모두 저장
    public <K, V> V writeThrough(String cacheName, K key, V value, UnaryOperator<V> dbWriteFunction){
        V saved = dbWriteFunction.apply(value);
        put(cacheName, key, saved);
        return saved;
    }

    // 캐시의 내용을 DB에 적용하고 캐시에서는 삭제
    public <K, V> V flush(String cacheName, K key, UnaryOperator<V> dbWriteFunction){
        V cached = get(cacheName, key);
        return dbWriteFunction.apply(cached);
    }

    // 캐시, DB 모두 삭제
    public <K> void deleteThrough(String cacheName, K key, Consumer<K> dbDeleteFunction){
        dbDeleteFunction.accept(key);
        evict(cacheName, key);
    }

    // 캐시, DB 모두 삭제 (복수)
    public <K> void deleteAllThroughByKeys(String cacheName, List<K> keys, Consumer<List<K>> dbDeleteFunction){
        dbDeleteFunction.accept(keys);
        evictAllByKeys(cacheName, keys);
    }


    // 캐시 조회 (단수)
    public <K, V> V get(String cacheName, K key){
        String cacheKey = getCacheKey(cacheName, key);
        return (V)ops.get(cacheKey);
    }

    // 캐시 조회 (복수)
    public <K, V> List<V> getAllByKeys(String cacheName, List<K> keys){

        return keys.stream().map(key -> {
                String cacheKey = getCacheKey(cacheName, key);
                return (V)ops.get(cacheKey);
            }).collect(Collectors.toList());
    }

    // 캐시 입력 (단수)
    public <K, V> void put(String cacheName, K key, V value){
        String cacheKey = getCacheKey(cacheName, key);
        ops.set(cacheKey, value);
    }

    // 캐시 삭제 (단수)
    public <K> void evict(String cacheName, K key){
        String cacheKey = getCacheKey(cacheName, key);
        redisTemplate.delete(cacheKey);
    }

    // 캐시 삭제 (복수)
    public <K> void evictAllByKeys(String cacheName, List<K> keys){
        List<String> cacheKeys = keys.stream()
                .map(key -> getCacheKey(cacheName, key))
                .collect(Collectors.toList());
        redisTemplate.delete(cacheKeys);
    }

    private <K> String getCacheKey(String cacheName, K key){
        return cacheName + ":" + key;
    }

}
