package com.dambae200.dambae200.global.cache.config;
import static com.dambae200.dambae200.global.cache.config.TimeConst.*;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CacheType {

    CIGARETTE("Cigarette", ONE_DAY),
    SESSION_INFO("SessionInfo", ONE_HOUR),
    ACCESS("Access", ONE_HOUR),
    CIGARETTE_LIST("CigaretteList", ONE_HOUR),
    CIGARETTE_DIRTY("CigaretteDirty", ONE_HOUR),
    STORE("Store", ONE_HOUR),
    USER("User",ONE_HOUR),
    NOTIFIACTION("Notification", ONE_MINUTE * 10),
    TEST("Test", ONE_DAY);

    private String cacheName;
    private int ttlSecond;

}
