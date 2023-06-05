package com.dambae200.dambae200.domain.sessionInfo.dto;

import com.dambae200.dambae200.domain.sessionInfo.domain.SessionInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
public class SessionInfoDto implements Serializable {

    private String accessToken;
    private Long userId;
    private String userAgent;
    private LocalDateTime expirationTime;

    public SessionInfoDto(SessionInfo sessionInfo){
        this.accessToken = sessionInfo.getAccessToken();
        this.userId = sessionInfo.getUserId();
        this.userAgent = sessionInfo.getUserAgent();
        this.expirationTime = sessionInfo.getExpirationTime();
    }

    public static SessionInfo toEntity(SessionInfoDto dto){
        return new SessionInfo.Builder(dto.getAccessToken(), dto.getUserId())
                .userAgent(dto.getUserAgent())
                .expirationTime(dto.getExpirationTime())
                .build();
    }


}
