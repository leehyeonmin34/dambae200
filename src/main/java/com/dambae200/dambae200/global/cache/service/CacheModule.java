package com.dambae200.dambae200.global.cache.service;
import static com.dambae200.dambae200.global.cache.service.CacheKeyGenerator.*;

import com.dambae200.dambae200.global.cache.config.CacheType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
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
    public <K, V> V getCacheOrLoad(CacheType cacheType, K key, Function<K, V> dbLoadFunction) {

        String cacheKey = getCacheKey(cacheType, key);
        V cached = (V) ops.get(cacheKey);

        // 캐시에 값 있을 때 바로 리턴
        if (cached != null) {
            log.info(String.format("캐시 사용 - cachaName: %s, key: %s", cacheType.getCacheName(), key));
            return (V) cached;
        }

        // 캐시에 값 없을 땐 다른 저장소 조회, 캐시에 저장
        V loaded = dbLoadFunction.apply(key);
        ops.set(key, loaded, cacheType.getTtlSecond(), TimeUnit.SECONDS);
        return loaded;
    }

    // 복수개의 Key들을 입력받아 조회
    public <K, V> List<V> getAllCacheOrLoadByKeys(CacheType cacheType
            , List<K> keys
            , Function<List<K>
            , List<V>> dbLoadFunction){


        List<K> notCachedKeys = new ArrayList<>();
        List<V> result = new ArrayList<>();

        keys.forEach(key -> {
            String cacheKey = getCacheKey(cacheType, key);
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
    public <K, V> V writeThrough(CacheType cacheType, K key, V value, UnaryOperator<V> dbWriteFunction){
        V saved = dbWriteFunction.apply(value);
        put(cacheType, key, saved);
        return saved;
    }

    // 캐시의 내용을 DB에 적용하고 캐시에서는 삭제
    public <K, V> V flush(CacheType cacheType, K key, UnaryOperator<V> dbWriteFunction){
        V cached = (V)ops.getAndDelete(getCacheKey(cacheType, key));
        return dbWriteFunction.apply(cached);
    }

    // 캐시, DB 모두 삭제
    public <K> void deleteThrough(CacheType cacheType, K key, Consumer<K> dbDeleteFunction){
        dbDeleteFunction.accept(key);
        evict(cacheType, key);
    }

    // 캐시, DB 모두 삭제 (복수)
    public <K> void deleteAllThroughByKeys(CacheType cacheType, List<K> keys, Consumer<List<K>> dbDeleteFunction){
        dbDeleteFunction.accept(keys);
        evictAllByKeys(cacheType, keys);
    }


    // 캐시 조회 (단수)
    public <K, V> V get(CacheType cacheType, K key){
        String cacheKey = getCacheKey(cacheType, key);
        return (V)ops.get(cacheKey);
    }

    // 캐시 조회 (복수)
    public <K, V> List<V> getAllByKeys(CacheType cacheType, List<K> keys){

        return keys.stream().map(key -> {
                String cacheKey = getCacheKey(cacheType, key);
                return (V)ops.get(cacheKey);
            }).collect(Collectors.toList());
    }

    // 캐시 입력 (단수)
    public <K, V> void put(CacheType cacheType, K key, V value){
        String cacheKey = getCacheKey(cacheType, key);
        ops.set(cacheKey, value, cacheType.getTtlSecond(), TimeUnit.SECONDS);
    }

    // 캐시 삭제 (단수)
    public <K> void evict(CacheType cacheType, K key){
        String cacheKey = getCacheKey(cacheType, key);
        redisTemplate.delete(cacheKey);
    }

    // 캐시 삭제 (복수)
    public <K> void evictAllByKeys(CacheType cacheType, List<K> keys){
        List<String> cacheKeys = keys.stream()
                .map(key -> getCacheKey(cacheType, key))
                .collect(Collectors.toList());
        redisTemplate.delete(cacheKeys);
    }



}
