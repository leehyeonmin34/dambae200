package com.dambae200.dambae200.global.service;

import com.dambae200.dambae200.domain.sessionInfo.domain.SessionInfo;
import com.dambae200.dambae200.global.config.CacheEnv;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;


@Component
@RequiredArgsConstructor
public class CacheModule {

    final private CacheManager cacheManager;

//    cacheName, key에 해당하는 캐시들을 먼저 조회한 뒤, 캐시에 없으면 다른 저장소 조회(loadFunction 수행)
    public <K, V> V getCacheOrLoad(String cacheName, K key, Function<K, V> dbLoadFunction) {

        Cache cache = getCache(cacheName);
        Cache.ValueWrapper cached = cache.get(key);

        // 캐시에 값 있을 때 바로 리턴
        if (cached != null)
            return (V) cached.get();

        // 캐시에 값 없을 땐 다른 저장소 조회, 캐시에 저장
        V loaded = dbLoadFunction.apply(key);
        cache.put(key, loaded);
        return loaded;
    }


    // 캐시, DB에 모두 저장
    public <K, V> V writeThrough(String cacheName, K key, V value, UnaryOperator<V> dbWriteFunction){
        V saved = dbWriteFunction.apply(value);
        put(cacheName, key, saved);
        return saved;
    }

    public <K, V> V flush(String cacheName, K key, UnaryOperator<V> dbWriteFunction){
        V cached = get(cacheName, key);
        return dbWriteFunction.apply(cached);
    }


    // 캐시, DB 모두 삭제
    public <K> void deleteThrough(String cacheName, K key, Consumer<K> dbDeleteFunction){
        dbDeleteFunction.accept(key);
        evict(cacheName, key);
    }


    public <K, V> V get(String cacheName, K key){
        Cache cache = getCache(cacheName);
        return (V)cache.get(key).get();
    }

    public <K, V> void put(String cacheName, K key, V value){
        Cache cache = getCache(cacheName);
        cache.put(key, value);
    }

    public <K> void evict(String cacheName, K key){
        Cache cache = getCache(cacheName);
        cache.evictIfPresent(key);
    }

    private Cache getCache(String cacheName){
        return Optional.ofNullable(cacheManager.getCache(cacheName))
                .orElseThrow(() -> new IllegalArgumentException(
                    String.format("선언되지 않은 캐시 이름(%s)입니다.", cacheName))
                );
    }

}
