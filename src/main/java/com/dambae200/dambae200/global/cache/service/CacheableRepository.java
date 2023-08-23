package com.dambae200.dambae200.global.cache.service;

import com.dambae200.dambae200.global.cache.config.CacheType;
import com.dambae200.dambae200.global.common.dto.DtoConverter;
import com.dambae200.dambae200.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CacheableRepository<E, V, K, REPO extends JpaRepository<E, K>> {

    // 도메인 속성에 따라 다른 공통 인자들
    final private CacheType cacheType;
    final private REPO repository;
    final private Function<E, K> entityKeyExtractor;
    final private Function<V, K> valueKeyExtractor;
    final private CacheModule cacheModule;
    final private Class<V> valueClass; // 캐시에 저장되는 클래스는 E를 인자로 갖는 생성자가 꼭 있어야 한다.
    final private Class<E> entityClass;


    public V getCacheOrLoad(K key){
        return (V)cacheModule.getCacheOrLoad(cacheType, key
                , (_id) -> DtoConverter.toDto(repository.findById(_id).orElseThrow(() -> new EntityNotFoundException(key.toString())), valueClass, entityClass));
    }

    public List<V> getAllCacheOrLoad(List<K> keys){
        return cacheModule.getAllCacheOrLoad(cacheType, keys, _keys -> {
            return DtoConverter.toDtoList(repository.findAllById(_keys), valueClass, entityClass);
        });
    }

    public E getEntityCacheOrLoad(K key){
        return (E)cacheModule.getEntityCacheOrLoad(cacheType, key
                , (_id) -> repository.findById(_id).orElseThrow(() -> new EntityNotFoundException(key.toString())), valueClass, entityClass);
    }

    public List<E> getAllEntityCacheOrLoad(List<K> keys){
        return cacheModule.getAllEntityCacheOrLoad(cacheType, keys, repository::findAllById, valueClass, entityClass);
    }



    public List<E> writeAllThrough(List<E> entities){
        return entities.stream().map(entity ->
                writeThrough(entityKeyExtractor.apply(entity), entity))
                .collect(Collectors.toList());
    }

    public E writeThrough(K cacheKey, E entity){
        return cacheModule.writeThrough(cacheType, cacheKey, entity, repository::save, valueClass, entityClass);
    }

    public E writeThrough(E entity){
        return cacheModule.writeThrough(cacheType, entityKeyExtractor.apply(entity), entity, repository::save, valueClass, entityClass);
    }

    public List<E> writeAllthrough(List<E> entities){
        return cacheModule.writeAllThrough(cacheType, entities
                , _entities -> repository.saveAll(_entities)
                .stream().collect(Collectors.toList())
                        , valueKeyExtractor, valueClass, entityClass);
    }

    public List<E> writeAllthroughPipelined(List<E> entities){
        return cacheModule.writeAllThroughPipelined(cacheType, entities
                , _entities -> repository.saveAll(_entities)
                        .stream().collect(Collectors.toList())
        , valueKeyExtractor, valueClass, entityClass);
    }

    public void deleteThrough(K key){
        cacheModule.deleteThrough(cacheType, key, repository::deleteById);
    }

    public void deleteAllThrough(List<K> keys){
        keys.forEach(key -> deleteThrough(key));
    }

    public void deleteAllThroughPipelined(List<K> keys){
        cacheModule.deleteAllThroughByKeysPipelined(cacheType, keys, repository::deleteAllByIdInBatch);
    }

    public V get(K key){
        return cacheModule.get(cacheType, key);
    }

    public List<V> getAllByKeys(List<K> keys){
        return cacheModule.getAllByKeys(cacheType, keys);
    }

    public void put(K key, V value){
        cacheModule.put(cacheType, key, value);
    }

    public void putAll(List<V> values){
        cacheModule.putAll(cacheType, values, valueKeyExtractor);
    }

    public void putAllPipelined(List<V> values){
        cacheModule.putAllPipelined(cacheType, values, valueKeyExtractor);
    }

    public void evict(K key){
        cacheModule.evict(cacheType, key);
    }

    public void evictAllByKeys(List<K> keys){
        cacheModule.evictAllByKeys(cacheType, keys);
    }

    public void evictAllByKeysPipelined(List<K> keys){
        cacheModule.evictAllByKeysPipelined(cacheType, keys);
    }

}
