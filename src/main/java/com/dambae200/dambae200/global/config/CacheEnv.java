package com.dambae200.dambae200.global.config;

import com.dambae200.dambae200.domain.sessionInfo.repository.SessionInfoRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CacheEnv {

    final private SessionInfoRepository sessionInfoRepository;

    public static final int ONE_DAY = 60 * 60 * 24;
    public static final int ONE_HOUR = 60 * 60;
    public static final int DEFAULT_EXPIRE_SEC = ONE_DAY; // 1 day

    public static final String SESSION_INFO = "SessionInfo";
    public static final int SESSION_INFO_EXPIRE_SEC = ONE_DAY;

    public static final String CIGARETTE_LIST = "CigaretteList";
    public static final int CIGARETTE_LIST_SEC = ONE_HOUR;

    public static final String CIGARETTE_DIRTY = "CigaretteDirty";
    public static final int CIGARETTE_DIRTY_SEC = ONE_HOUR;

    public static final String TEST = "test";




}
