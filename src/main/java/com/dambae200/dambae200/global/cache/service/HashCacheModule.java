package com.dambae200.dambae200.global.cache.service;
import static com.dambae200.dambae200.global.cache.service.CacheKeyGenerator.*;

import com.dambae200.dambae200.global.cache.config.CacheType;
import com.dambae200.dambae200.global.common.dto.DtoConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
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
    @Transactional(readOnly = true)
    public <K, HK, V> V getCacheOrLoad(CacheType cacheType, K key, HK hashKey, Function<HK, V> loadDtoFunction) {

        // 캐시가 있다면 바로 리턴
        if (ops.hasKey(getCacheKey(cacheType, key), hashKey)) {
            V cached = get(cacheType, key, hashKey);
            log.info(String.format("캐시 사용 - cachaName: %s, key: %s, hashKey: %s", cacheType.getCacheName(), key.toString(), hashKey.toString()));
            return cached;
        }

        // 캐시가 없을 땐 다른 저장소 조회, 캐시에 저장
        V loaded = loadDtoFunction.apply(hashKey);
        put(cacheType, key, hashKey, loaded);
        return loaded;
    }

    // cacheName, key에 해당하는 캐시들을 먼저 조회한 뒤, 캐시에 없으면 다른 저장소 조회(loadFunction 수행)
    // key에 해당하는 map 전체에 대한 look-aside
    @Transactional(readOnly = true)
    public <K, HK, V> Map<HK, V> getAllCacheOrLoad(CacheType cacheType, K key, Function<K, List<V>> dbLoadFunctionByKey, Function<V, HK> keyExtractor){
        Map<HK, V> cached = getAll(cacheType, key);

        // 캐시에 값 있을 때 바로 리턴
        if (!cached.isEmpty()) {
            log.info(String.format("캐시 사용 - cachaName: %s, key: %s", cacheType.getCacheName(), key.toString()));
            return cached;
        }

        // 캐시에 값 없을 땐 다른 저장소 조회, 캐시에 저장
        List<V> loaded = dbLoadFunctionByKey.apply(key);
        log.info("DB에서 불러옴" + loaded);
        putAll(cacheType, key, loaded, keyExtractor);

        // map으로 변환 후 리턴
        return loaded.stream()
                .collect(Collectors.toUnmodifiableMap(keyExtractor, item -> item));
    }

    // cacheName, key, hashKeys에 해당하는 캐시들을 먼저 조회한 뒤, 캐시에 없으면 다른 저장소 조회(loadFunction 수행)
    // map entry에 대한 look-aside (복수건)
    @Transactional(readOnly = true)
    public <K, HK, V> Map<HK, V> getAllCacheOrLoadByHashKeys(CacheType cacheType
            , K key
            , List<HK> hashKeys
            , Function<List<HK>, List<V>> dbLoadFunctionByHashKey
            , Function<V, HK> keyExtractor){
        String cacheKey = getCacheKey(cacheType, key);

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
            putAll(cacheType, key, loaded, keyExtractor);
            result.addAll(loaded);
        }

        // map으로 변환 후 리턴
        return result.stream()
                .collect(Collectors.toUnmodifiableMap(keyExtractor, item -> item));
    }

    // cacheName, key, hashKey에 해당하는 캐시를 먼저 조회한 뒤, 캐시에 없으면 다른 저장소 조회(loadFunction 수행)
    // map의 entry에 대한 look-aside (단건)
    @Transactional(readOnly = true)
    public <K, HK, V, E> E getEntityCacheOrLoad(CacheType cacheType
                                                , K key
                                                , HK hashKey
                                                , Function<HK, E> loadDtoFunction
                                                , Class<V> valueClass
                                                , Class<E> entityClass) {

        // 캐시가 있다면 바로 리턴
        if (ops.hasKey(getCacheKey(cacheType, key), hashKey)) {
            V cached = get(cacheType, key, hashKey);
            log.info(String.format("캐시 사용 - cachaName: %s, key: %s, hashKey: %s", cacheType.getCacheName(), key.toString(), hashKey.toString()));
            return DtoConverter.toEntity(cached, valueClass, entityClass);
        }

        // 캐시가 없을 땐 다른 저장소 조회, 캐시에 저장
        E loaded = loadDtoFunction.apply(hashKey);
        V value = DtoConverter.toDto(loaded, valueClass, entityClass);
        put(cacheType, key, hashKey, value);
        return loaded;
    }

    // cacheName, key에 해당하는 캐시들을 먼저 조회한 뒤, 캐시에 없으면 다른 저장소 조회(loadFunction 수행)
    // key에 해당하는 map 전체에 대한 look-aside
    @Transactional(readOnly = true)
    public <K, HK, V, E> Map<HK, E> getAllEntityCacheOrLoad(CacheType cacheType
                                                            , K key
                                                            , Function<K, List<E>> dbLoadFunctionByKey
                                                            , Function<V, HK> keyExtractor
                                                            , Class<V> valueClass
                                                            , Class<E> entityClass){
        Map<HK, V> cached = getAll(cacheType, key);

        // 캐시에 값 있을 때 바로 리턴
        if (!cached.isEmpty()) {
            log.info(String.format("캐시 사용 - cachaName: %s, key: %s", cacheType.getCacheName(), key.toString()));
            return cached.values().stream().collect(Collectors.toUnmodifiableMap(keyExtractor, item -> DtoConverter.toEntity(item, valueClass, entityClass)));
        }

        // 캐시에 값 없을 땐 다른 저장소 조회, 캐시에 저장
        List<E> loaded = dbLoadFunctionByKey.apply(key);
        log.info("DB에서 불러옴" + loaded);
        List<V> dtoList = DtoConverter.toDtoList(loaded, valueClass, entityClass);
        putAll(cacheType, key, dtoList, keyExtractor);

        // map으로 변환 후 리턴
        return dtoList.stream()
                .collect(Collectors.toUnmodifiableMap(keyExtractor, item -> DtoConverter.toEntity(item, valueClass, entityClass)));
    }

    // cacheName, key, hashKeys에 해당하는 캐시들을 먼저 조회한 뒤, 캐시에 없으면 다른 저장소 조회(loadFunction 수행)
    // map entry에 대한 look-aside (복수건)
    @Transactional(readOnly = true)
    public <K, HK, V, E> Map<HK, E> getAllEntityCacheOrLoadByHashKeys(CacheType cacheType
            , K key
            , List<HK> hashKeys
            , Function<List<HK>, List<E>> dbLoadFunctionByHashKey
            , Function<V, HK> valueKeyExtractor
            , Function<E, HK> entityKeyExtractor
            , Class<V> valueClass
            , Class<E> entityClass){
        String cacheKey = getCacheKey(cacheType, key);

        List<E> result = new ArrayList<>();
        List<HK> notCachedHashKeys = new ArrayList<>();

        hashKeys.forEach(hashKey -> {
            V cached = (V)ops.get(cacheKey, hashKey);
            if (cached != null) result.add(DtoConverter.toEntity(cached, valueClass, entityClass));
            else notCachedHashKeys.add(hashKey);
        });

        // 캐시에 없는 값은 DB 조회, 캐시에 저장 및 결과 병합
        if(!notCachedHashKeys.isEmpty()) {
            List<E> loaded = dbLoadFunctionByHashKey.apply(notCachedHashKeys);
            List<V> dtoList = DtoConverter.toDtoList(loaded, valueClass, entityClass);
            putAll(cacheType, key, dtoList, valueKeyExtractor);
            result.addAll(loaded);
        }

        // map으로 변환 후 리턴
        return result.stream()
                .collect(Collectors.toUnmodifiableMap(entityKeyExtractor, item -> item));
    }

    // 캐시 조회 (단건)
    @Transactional(readOnly = true)
    public <K, HK, V> V get(CacheType cacheType, K key, HK hashKey){
        String cacheKey = getCacheKey(cacheType, key);
        return (V)ops.get(cacheKey, hashKey);
    }

    // 캐시 조회 (key에 대한 map 전체 조회)
    @Transactional(readOnly = true)
    public <K, HK, V> Map<HK, V> getAll(CacheType cacheType, K key){
        String cacheKey = getCacheKey(cacheType, key);
        return ops.entries(cacheKey);
    }



    // 캐시, DB에 모두 저장 (단건)
    public <K, HK, V, E> E writeThrough(CacheType cacheType, K key, E entity, Function<V, HK> keyExtractor,  UnaryOperator<E> dbWriteFunction, Class<V> valueClass, Class<E> entityClass){
        E saved = dbWriteFunction.apply(entity);
        V value = DtoConverter.toDto(saved, valueClass, entityClass);
        HK hashKey = keyExtractor.apply(value);
        put(cacheType, key, hashKey, value);
        return saved;
    }

    // 캐시, DB에 모두 저장 (복수건)
    public <K, HK, V, E> List<E> writeAllThrough(CacheType cacheType, K key, List<E> entities, Function<V, HK> keyExtractor, UnaryOperator<List<E>> dbWriteFunction, Class<V> valueClass, Class<E> entityClass){
        List<E> saved = dbWriteFunction.apply(entities);
        List<V> dtoList = DtoConverter.toDtoList(saved, valueClass, entityClass);
        putAll(cacheType, key, dtoList, keyExtractor);
        return saved;
    }



    // 캐시 내용을 DB에 적용 후 캐시 삭제 (단건)
    public <K, HK, V, E> E flush(CacheType cacheType, K key, HK hashKey, UnaryOperator<E> dbWriteFunction, Class<V> valueClass, Class<E> entityClass){
        V cached = get(cacheType, key, hashKey);
        return dbWriteFunction.apply(DtoConverter.toEntity(cached, valueClass, entityClass));
    }

    // 캐시 내용을 DB에 적용 후 캐시 삭제 (복수건)
    public <K, HK, V, E> List<E> flushAll(CacheType cacheType, K key, UnaryOperator<List<E>> dbWriteFunction, Class<V> valueClass, Class<E> entityClass){
        List<V> cached = ((Map<HK, V>)getAll(cacheType, key))
                .values().stream().collect(Collectors.toList());
        List<E> saved = dbWriteFunction.apply(DtoConverter.toEntityList(cached, valueClass, entityClass));
        evictAll(cacheType, key);
        return saved;
    }



    // 캐시 입력 (단건)
    public <K, HK, V> void put(CacheType cacheType, K key, HK hashKey, V value){
        String cacheKey = getCacheKey(cacheType, key);
        log.info("캐시 저장" + value.toString());
        ops.put(cacheKey, hashKey, value);
        redisTemplate.expire(cacheKey, cacheType.getTtlSecond(), TimeUnit.SECONDS);
    }

    // 캐시 입력 (복수건)
    public <K, HK, V> void putAll(CacheType cacheType, K key, List<V> values, Function<V, HK> keyExtractor){
        String cacheKey = getCacheKey(cacheType, key);
        Map<HK, V> map = values.stream().collect(Collectors.toUnmodifiableMap(keyExtractor, item -> {
            log.info("캐시 저장" + item.toString());
            return item;
        }));
        ops.putAll(cacheKey, map);
        redisTemplate.expire(cacheKey, cacheType.getTtlSecond(), TimeUnit.SECONDS);
    }

    // 캐시 삭제 (단건)
    public <K, HK> void evict(CacheType cacheType, K key, HK hashKey){
        String cacheKey = getCacheKey(cacheType, key);
        ops.delete(cacheKey, hashKey);
    }

    // 캐시 삭제 (복수건)
    public <K, HK> void evictAllByHashKeys(CacheType cacheType, K key, List<HK> hashKeys){
        String cacheKey = getCacheKey(cacheType, key);
        for(HK hashKey: hashKeys)
            ops.delete(cacheKey, hashKey);
    }

    // 캐시 삭제 (key에 해당하는 map 전체)
    public <K, HK> void evictAll(CacheType cacheType, K key){
        String cacheKey = getCacheKey(cacheType, key);
        Set<HK> hashKeys = ops.entries(cacheKey).keySet();
        for(HK hashKey: hashKeys)
            ops.delete(cacheKey, hashKey);
    }

    // 캐시, DB 모두 삭제 (단건)
    public <K, HK> void deleteThrough(CacheType cacheType, K key, HK hashKey, Consumer<HK> dbDeleteFunction){
        evict(cacheType, key, hashKey);
        dbDeleteFunction.accept(hashKey);
    }

    // 캐시, DB 모두 삭제 (key에 해당하는 map 전체)
    public <K, HK> void deleteAllThrough(CacheType cacheType, K key, Consumer<K> dbDeleteFunction){
        evictAll(cacheType, key);
        dbDeleteFunction.accept(key);
    }

    // 캐시, DB 모두 삭제 (복수건)
    public <K, HK> void deleteAllThroughByHashKeys(CacheType cacheType, K key, List<HK> hashKeys, Consumer<List<HK>> dbDeleteFunction){
        evictAllByHashKeys(cacheType, key, hashKeys);
        dbDeleteFunction.accept(hashKeys);
    }


}
