package com.dambae200.dambae200.global.cache.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableCaching
@EnableTransactionManagement
public class RedisConfig {
    @Value("${spring.redis.host}")
    public String host;

    @Value("${spring.redis.port}")
    public int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisSerializer redisKeySerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

//    @Bean
//    Hibernate5Module hibernate5Module(){
//        Hibernate5Module hibernate5Module = new Hibernate5Module();
//        hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
//        return hibernate5Module;
//    }

    // Java8 Date/Time 타입을 직렬,역직렬화할 수 있는 GenericJackson 기반
    @Bean
    public RedisSerializer redisValueSerializer() {
        PolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator
                .builder()
                .allowIfSubType(Object.class)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL);

        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false); // 비어있는 객체는 읽지 않음(LazyLoading 프록시같은)
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 없는 필드는 읽지 않음 (redis에 의해 추가되는 expired 필드 무시)

        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }

    @Bean(name = "cacheManager")
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, RedisSerializer redisKeySerializer, RedisSerializer redisValueSerializer) {

        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues() // null value 캐시안함
                .entryTtl(Duration.ofSeconds(CacheEnvOld.DEFAULT_EXPIRE_SEC)) // 캐시의 기본 유효시간 설정
                .computePrefixWith(CacheKeyPrefix.simple()) // prefix = "CacheName::"
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisKeySerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisValueSerializer))
                ; // redis 캐시 데이터 저장방식을 StringSeriallizer로 지정

        // 캐시키별 default 유효시간 설정
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        Arrays.stream(CacheType.values()).forEach(cacheObject -> {
            cacheConfigurations.put(cacheObject.getCacheName(), RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofSeconds(cacheObject.getTtlSecond())));
        });

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(connectionFactory)
                .cacheDefaults(configuration)
                .disableCreateOnMissingCache() // cacheManager.getCache(cacheName) 했을 때, 존재하지 않는 cacheName에 대한 Cache를 생성하지 않음
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate(RedisSerializer redisKeySerializer, RedisSerializer redisValueSerializer) {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setKeySerializer(redisKeySerializer);
        redisTemplate.setValueSerializer(redisValueSerializer);
        redisTemplate.setHashKeySerializer(redisKeySerializer);
        redisTemplate.setHashValueSerializer(redisValueSerializer);
        return redisTemplate;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }
}
