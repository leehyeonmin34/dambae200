package com.dambae200.dambae200.global.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class RedisTxServiceTest {

    @Autowired
    private RedisTxService redisTxService;

    @Autowired
    private RedisTemplate redisTemplate;

    private final String key = "txKey";
    private final String copyKey = "copyKey";

    @BeforeEach
    void setUp() {
        redisTemplate.opsForValue().set(key, 1); // test마다 시도 전에 txKey의 값을 1로 설정합니다.
    }

    @Test
    @DisplayName("예외가 발생하지 않으면, 정상적으로 key의 값이 1 증가한다.")
    void incr_tx_test() {
        // when
        redisTxService.incr(key, false); // 예외 발생 X

        // then
        int value = (Integer)redisTemplate.opsForValue().get(key); // 결과 조회
        assertThat(value).isEqualTo(2); // 1 증가 성공
    }

    @Test
    @DisplayName("트랜잭션안에서 exception이 발생하면 transaction이 discard된다.")
    void incr_tx_test_throw_exception() {
        // when
        assertThatThrownBy(() -> redisTxService.incr(key, true)) // 예외 발생
                .isInstanceOf(RuntimeException.class); // RuntimeException 발생
        // then
        int value = (Integer)redisTemplate.opsForValue().get(key);
        assertThat(value).isEqualTo(1); // 기존 그대로의 값
    }

    @Test
    @DisplayName("트랜잭션 내부에서 값을 조회해서 활용하고자하면 Exception이 발생한다.")
    void incrAndCopy_txTest_exception() {

        redisTxService.incrAndCopy(key, copyKey, 10);
//        assertThatThrownBy(() -> redisTxService.incrAndCopy(key, copyKey, 10))
//                .isInstanceOf(IllegalArgumentException.class); // null값을 조작하려고해서 IllegalArgumentException 발생

        //then
        int value = (Integer)redisTemplate.opsForValue().get(key);
        int copied = (Integer)redisTemplate.opsForValue().get(copyKey);
        assertThat(value).isEqualTo(11); // 값 변동없음
        assertThat(copied).isEqualTo(11); // 값 변동없음

    }

}