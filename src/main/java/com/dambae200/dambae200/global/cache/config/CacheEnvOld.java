package com.dambae200.dambae200.global.cache.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CacheEnvOld {

//    final private SessionInfoRepository sessionInfoRepository;

    public static final int ONE_DAY = 60 * 60 * 24;
    public static final int ONE_HOUR = 60 * 60;
    public static final int ONE_MINUTE = 60;
    public static final int DEFAULT_EXPIRE_SEC = ONE_DAY; // 1 day

    // 모든 사용자 공용 데이터이므로 길게
    public static final String CIGARETTE = "Cigarette";
    public static final int CIGARETTE_SEC = ONE_DAY;

    // 검수 시간(사용 시간) 넉넉 잡아 1시간이라 가정해,
    // 검수에 필요한 정보들은 1시간으로 TTL 설정
    public static final String SESSION_INFO = "SessionInfo";
    public static final int SESSION_INFO_EXPIRE_SEC = ONE_HOUR;

    public static final String ACCESS = "Access";
    public static final int ACCESS_SEC = ONE_HOUR;

    public static final String CIGARETTE_LIST = "CigaretteList";
    public static final int CIGARETTE_LIST_SEC = ONE_HOUR;

    public static final String CIGARETTE_DIRTY = "CigaretteDirty";
    public static final int CIGARETTE_DIRTY_SEC = ONE_HOUR;

    public static final String STORE = "Store";
    public static final int STORE_SEC = ONE_HOUR;

    public static final String USER = "User";
    public static final int USER_SEC = ONE_HOUR;

    public static final String NOTIFICATION = "Notification";
    public static final int NOTIFICATION_SEC = ONE_MINUTE * 10;



    public static final String TEST = "test";




}
