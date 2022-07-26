package com.dambae200.dambae200.domain.sessionInfo.service;


import com.dambae200.dambae200.domain.sessionInfo.domain.SessionInfo;
import com.dambae200.dambae200.domain.sessionInfo.exception.SessionInfoNotExistsException;
import com.dambae200.dambae200.domain.user.dto.UserGetResponse;

public interface SessionService {
    boolean existsByToken(String accessToken);

    SessionInfo getSessionElseThrow(String accessToken);

    SessionInfo registerSession(UserGetResponse user, String userAgent);

    void removeSession(String accessToken);

    default void checkValidation(String accessToken){
        if (accessToken == null || !existsByToken(accessToken))
            throw new SessionInfoNotExistsException();
    };
}
