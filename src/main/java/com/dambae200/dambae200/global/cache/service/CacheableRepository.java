package com.dambae200.dambae200.global.cache.service;

import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import com.dambae200.dambae200.global.cache.config.CacheEnv;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CacheableRepository<K, V, REPO extends JpaRepository<V, K>> {

    // 도메인 공통 인자들
    final private String cacheName;
    final private REPO repository;
    final private Function<V, K> keyExtractor;
    final private CacheModule cacheModule;

    // 모든 도메인에 쓰일 수 있을만한 공통 로직
    public List<V> getAllCacheOrLoadByKeys(List<K> keys){
        return cacheModule.getAllCacheOrLoadByKeys(cacheName, keys, repository::findAllById);
    }


    public V getCacheOrLoad(K key){
        return (V)cacheModule.getCacheOrLoad(cacheName, key
                , (_id) -> repository.findById(_id).orElseThrow(EntityNotFoundException::new));
    }

    public void evict(K key){
        cacheModule.evict(cacheName, key);
    }


    public List<V> writeAllThrough(List<V> values){
        return values.stream().map(value ->
                writeThrough(keyExtractor.apply(value), value))
                .collect(Collectors.toList());
    }

    public V writeThrough(K key, V value){
        return cacheModule.writeThrough(cacheName, key, value, repository::save);
    }


    public V writeThrough(V value){
        return cacheModule.writeThrough(cacheName, keyExtractor.apply(value), value, repository::save);
    }

    public void deleteThrough(K key){
        cacheModule.deleteThrough(cacheName, key, repository::deleteById);
    }

    public void deleteAllThrough(List<K> keys){
        keys.forEach(key -> deleteThrough(key));
    }

}
