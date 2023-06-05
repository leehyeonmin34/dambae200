package com.dambae200.dambae200.global.rlock;

import com.dambae200.dambae200.global.cache.config.CacheType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RLockType {
    CIGARETTE_LIST(CacheType.CIGARETTE_LIST.getCacheName() + ":RLock:%s", 2, 5)
    ;

    private String format;
    private long waitSeconds; // lock을 얻기 위해 기다리는 시간(second, default 0초)
    private long leaseSeconds; // lock을 보관하고 있는 시간(second, default 5초)

    public String getLockKey(String key){
        return String.format(getFormat(), key);
    }

}