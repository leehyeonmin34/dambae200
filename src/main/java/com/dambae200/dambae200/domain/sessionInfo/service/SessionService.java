package com.dambae200.dambae200.domain.sessionInfo.service;


import com.dambae200.dambae200.domain.sessionInfo.domain.SessionInfo;
import com.dambae200.dambae200.domain.user.dto.UserDto;

public interface SessionService {
    boolean existsByToken(String accessToken);

    SessionInfo getSessionElseThrow(String accessToken);

    // 세션 키 등록
    SessionInfo registerSession(UserDto.GetResponse user, String userAgent);

    // 세션 키 삭제
    void removeSession(String accessToken);
}
