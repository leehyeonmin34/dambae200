package com.dambae200.dambae200.global.cache.service;

import com.dambae200.dambae200.global.cache.config.CacheType;
import com.dambae200.dambae200.global.common.dto.DtoConverter;
import com.dambae200.dambae200.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class HashCacheableRepository<E, V, K, HK, REPO extends JpaRepository<E, HK>> {

    final private CacheType cacheType;
    final private REPO repository;
    final private HashCacheModule hashCacheModule;
    final private Function<V, HK> valueKeyExtractor;
    final private Function<E, HK> entityKeyExtactor;
    final private Class<V> valueClass;
    final private Class<E> entityClass;

    public V getCacheOrLoad(K key, HK hashKey){
        return hashCacheModule.getCacheOrLoad(cacheType, key, hashKey, this::loadDtoFunction);
    }

    private V loadDtoFunction(HK hashKey){
        E entity = loadEntityFunction(hashKey);
        return DtoConverter.toDto(entity, valueClass, entityClass);
    }

    private E loadEntityFunction(HK hashKey){
        return repository.findById(hashKey)
                .orElseThrow(() -> new EntityNotFoundException(entityClass.toString(), hashKey.toString()));
    }

    public Map<HK, V> getAllCacheOrLoad(K key, Function<K, List<V>> loadDtoList){
        return hashCacheModule.getAllCacheOrLoad(cacheType, key, loadDtoList, valueKeyExtractor);
    }

    public Map<HK, V> getAllCacheOrLoadByHashKeys(K key, List<HK> hashKeys){
        return hashCacheModule.getAllCacheOrLoadByHashKeys(cacheType, key, hashKeys, idList -> DtoConverter.toDtoList(repository.findAllById(idList), valueClass, entityClass), valueKeyExtractor);
    }

    public E getEntityCacheOrLoad(K key, HK hashKey){
        return hashCacheModule.getEntityCacheOrLoad(cacheType, key, hashKey, this::loadEntityFunction, valueClass, entityClass);
    }

    public Map<HK, E> getAllEntityCacheOrLoad(K key, Function<K, List<E>> dbLoadFunctionByKey){
        return hashCacheModule.getAllEntityCacheOrLoad(cacheType, key, dbLoadFunctionByKey, valueKeyExtractor, valueClass, entityClass);
    }

    public Map<HK, E> getAllEntityCacheOrLoadByHashKeys(K key, List<HK> hashKeys){
        return hashCacheModule.getAllEntityCacheOrLoadByHashKeys(cacheType, key, hashKeys, repository::findAllById, valueKeyExtractor, entityKeyExtactor, valueClass, entityClass);
    }

    public V get(K key, HK hashKey){
        return hashCacheModule.get(cacheType, key, hashKey);
    }

    public Map<HK, V> getAll(K key){
        return hashCacheModule.getAll(cacheType, key);
    }

    public E writeThrough(K key, E entity){
        return hashCacheModule.writeThrough(cacheType, key, entity, valueKeyExtractor, repository::save, valueClass, entityClass);
    }

    public List<E> writeAllThrough(K key, List<E>  entities){
        return hashCacheModule.writeAllThrough(cacheType, key, entities, valueKeyExtractor, repository::saveAll, valueClass, entityClass);
    }

    public E flush(K key, HK hashKey){
        return hashCacheModule.flush(cacheType, key, hashKey, repository::save, valueClass, entityClass);
    }

    public List<E> flushAll(K key){
        return hashCacheModule.flushAll(cacheType, key, repository::saveAll, valueClass, entityClass);
    }

    public void put(K key, HK hashKey, V value){
        hashCacheModule.put(cacheType, key, hashKey, value);
    }

    public void putAll(K key, List<V> values){
        hashCacheModule.putAll(cacheType, key, values, valueKeyExtractor);
    }

    public void evict(K key, HK hashKey){
        hashCacheModule.evict(cacheType, key, hashKey);
    }

    public void evictAllByHashKeys(K key, List<HK> hashKeys){
        hashCacheModule.evictAllByHashKeys(cacheType, key, hashKeys);
    }

    public void evictAll(K key){
        hashCacheModule.evictAll(cacheType, key);
    }

    public void deleteThrough(K key, HK hashKey){
        hashCacheModule.deleteThrough(cacheType, key, hashKey, repository::deleteById);
    }

    public void deleteAllThrough(K key, Consumer<K> dbDeleteFunction){
        hashCacheModule.deleteAllThrough(cacheType, key, dbDeleteFunction);
    }

    public void deleteAllThroughByHashKeys(K key, List<HK> hashKeys){
        hashCacheModule.deleteAllThroughByHashKeys(cacheType, key, hashKeys, repository::deleteAllByIdInBatch);
    }


}
