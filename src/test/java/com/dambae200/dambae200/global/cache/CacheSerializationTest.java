package com.dambae200.dambae200.global.cache;

import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.global.cache.config.CacheEnv;
import com.dambae200.dambae200.global.cache.service.CacheModule;
import com.dambae200.dambae200.global.cache.service.HashCacheModule;
import org.hibernate.Cache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
public class CacheSerializationTest {

    @Autowired
    CacheModule cacheModule;

    @Autowired
    HashCacheModule hashCacheModule;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    CacheManager cacheManager;

    @Test
    public void serializationTest(){
        User testUser = new User("email", "pw","nickname");
        cacheModule.put(CacheEnv.TEST, "key", testUser.toString());
        cacheModule.put(CacheEnv.TEST, "Integer", "1");

        hashCacheModule.put(CacheEnv.TEST, "hash", "hashKey", testUser);



        String integerCache = cacheModule.get(CacheEnv.TEST, "Integer");
        String testUserString = cacheModule.get(CacheEnv.TEST, "key");
        then(testUserString).isEqualTo(testUser.toString());
        then(integerCache).isEqualTo("1");



    }

}
