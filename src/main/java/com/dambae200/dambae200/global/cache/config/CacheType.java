package com.dambae200.dambae200.global.cache.config;
import static com.dambae200.dambae200.global.cache.config.TimeConst.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisTemplate;

@Getter
@AllArgsConstructor
public enum CacheType {

    SESSION_INFO("SessionInfo", ONE_HOUR, RedisServerType.SESSION),

    CIGARETTE("Cigarette", ONE_DAY, RedisServerType.CACHE),
    ACCESS("Access", ONE_HOUR, RedisServerType.CACHE),
    CIGARETTE_LIST("CigaretteList", ONE_HOUR, RedisServerType.CACHE),
    CIGARETTE_DIRTY("CigaretteDirty", ONE_HOUR, RedisServerType.CACHE),
    STORE("Store", ONE_HOUR, RedisServerType.CACHE),
    USER("User",ONE_HOUR, RedisServerType.CACHE),
    NOTIFIACTION("Notification", ONE_MINUTE * 10, RedisServerType.CACHE),
    TEST("Test", ONE_DAY, RedisServerType.CACHE);

    private String cacheName;
    private int ttlSecond;
    private RedisServerType redisServerType;

}
