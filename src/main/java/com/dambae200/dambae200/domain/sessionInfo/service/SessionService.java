package com.dambae200.dambae200.domain.sessionInfo.service;


import com.dambae200.dambae200.domain.sessionInfo.domain.SessionInfo;
import com.dambae200.dambae200.domain.sessionInfo.exception.SessionInfoNotExistsException;
import com.dambae200.dambae200.domain.user.dto.UserGetResponse;

public interface SessionService {
    boolean existsByToken(final String accessToken);

    SessionInfo getSessionElseThrow(final String accessToken);

    SessionInfo registerSession(final UserGetResponse user, String userAgent);

    void removeSession(final String accessToken);

    default void checkValidation(final String accessToken){
        if (accessToken == null || !existsByToken(accessToken))
            throw new SessionInfoNotExistsException();
    };
}
