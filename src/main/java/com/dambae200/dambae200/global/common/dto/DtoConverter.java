package com.dambae200.dambae200.global.common.dto;

import com.dambae200.dambae200.global.error.exception.UnhandledServerException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class DtoConverter {

    // valueClass에서 entityClass를 인자로하는 생성자를 찾아 실행
    public static <V, E> V toDto(E entity, Class<V> valueClass, Class<E> entityClass){
        try {
            V value = valueClass.getDeclaredConstructor(entityClass).newInstance(entity);
//            log.info(entity.toString() + "->" + value.toString());
            return value;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new UnhandledServerException("DTO에서 Entity를 인자로하는 생성자를 찾을 수 없습니다.");
        }
    }

    public static <V, E> List<V> toDtoList(List<E> entities, Class<V> valueClass, Class<E> entityClass){
        return entities.stream()
                .map(entity -> toDto(entity, valueClass, entityClass))
                .collect(Collectors.toList());
    }

    // valueClass에서 valueClass를 인자로하는 "toEntity"라는 이름의 메서드를 찾아 실행
    public static <V, E> E toEntity(V value, Class<V> valueClass, Class<E> entityClass){
        try {
            E entity = (E) valueClass.getDeclaredMethod("toEntity", valueClass).invoke(null, value);
//            log.info(value.toString() + "->" + entity.toString());
            return entity;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            throw new UnhandledServerException("DTO에서 Entity로 변환하는 'toEntity' 메서드를 찾을 수 없습니다.");
        }
    }

    public static <V, E> List<E> toEntityList(List<V> values, Class<V> valueClass, Class<E> entityClass){
        return values.stream()
                .map(value -> toEntity(value, valueClass, entityClass))
                .collect(Collectors.toList());
    }

}
