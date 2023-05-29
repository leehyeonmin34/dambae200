package com.dambae200.dambae200.global.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional // 클래스 단위로 적용
@Slf4j
public class RedisTxService {

    private final RedisTemplate redisTemplate;

    public void incr(String key, boolean isException) {
        // 하나의 Strings 자료구조의 key의 value를 1 증가 시키는 메서드입니다.
        redisTemplate.opsForValue().increment(key);
        // 예외 상황을 위해서 isException이 true이면 RuntimeException을 던집니다.
        if(isException) {
            throw new RuntimeException();
        }

    }

    // 무조건 실패하는 로직입니다.
    public RedisDto incrAndCopy(String originkey, String newkey, int count) {
        // 기존키의 값을 count만큼 증가시킵니다.
        redisTemplate.opsForValue().increment(originkey, count);
        // 증가된 값을 가져옵니다.
        int value = (Integer)redisTemplate.opsForValue().get(originkey);
        log.info("after increment, get(originKey) = {}", value);

        // 새로운 key에 증가된 값을 저장합니다.
        redisTemplate.opsForValue().set(newkey, value);

        // 새로 저장된 key와 value를 Dto로 만들어 반환합니다.
        return new RedisDto(newkey, value);
    }

}