package com.dambae200.dambae200.global.cache;

import com.dambae200.dambae200.global.cache.config.CacheType;
import com.dambae200.dambae200.global.cache.service.CacheModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
public class CacheTransactionTest {

    @Autowired
    private CacheModule cacheModule;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TestCacheService testCacheService;

    @Test
    public void getOrLoadTest(){
        int cached = testCacheService.putAndGetOrLoad(1);
        then(cached).isEqualTo(1000);
    }

    @Test
    public void putAndGetOrLoadFailTest(){
        assertThatThrownBy(() -> testCacheService.putAndGetOrLoadFail(1))
                .isInstanceOf(RuntimeException.class);
        Integer cached = cacheModule.get(CacheType.TEST, 1);
        then(cached).isEqualTo(null);
    }

//    @Test
//    public void putAndGetOrLoadFailTest2(){
//        redisTemplate.execute((RedisCallback) operations -> {
//            System.out.println(1);
//           return null;
//        });
//    }

}
