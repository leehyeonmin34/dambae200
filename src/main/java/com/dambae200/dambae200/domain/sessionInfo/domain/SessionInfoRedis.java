package com.dambae200.dambae200.domain.sessionInfo.domain;

import com.dambae200.dambae200.domain.sessionInfo.domain.SessionInfo;
import com.dambae200.dambae200.domain.sessionInfo.exception.AccessedExpiredSessionTokenException;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@ToString
@RedisHash("sessionInfo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SessionInfoRedis implements Serializable {

    @Id
    @Column(name = "access_token", unique = true, nullable = false, updatable = false)
    private String accessToken;

    @Column(name = "user_id", nullable = false, updatable = false)
    @Getter
    private Long userId;

    @Column(name = "user_agent")
    @Getter
    private String userAgent;

    @Column(name = "expiration_time")
    @Getter
    private LocalDateTime expirationTime;


    @Getter
    public static class Builder{
        private String accessToken;
        private Long userId;
        private String userAgent;
        private LocalDateTime expirationTime;

        public Builder(String accessToken, Long userId){
            this.accessToken = accessToken;
            this.userId = userId;
        }

        public SessionInfoRedis.Builder userAgent(String userAgent){
            this.userAgent = userAgent;
            return this;
        }


        public SessionInfoRedis.Builder expirationTime(LocalDateTime expirationTime){
            this.expirationTime = expirationTime;
            return this;
        }

        public SessionInfoRedis build(){
            return new SessionInfoRedis(this);
        }

    }

    private SessionInfoRedis(SessionInfoRedis.Builder builder){
        this.accessToken = builder.getAccessToken();
        this.userId = builder.getUserId();
        this.userAgent = builder.getUserAgent();
        this.expirationTime = builder.getExpirationTime();
    }

    public static SessionInfoRedis from(SessionInfo sessionInfo){
        return new Builder(sessionInfo.getAccessToken(), sessionInfo.getUserId())
                .expirationTime(sessionInfo.getExpirationTime())
                .userAgent(sessionInfo.getUserAgent())
                .build();
    }

    public static SessionInfo toSessionInfo(SessionInfoRedis sessionInfoRedis){
        return new SessionInfo.Builder(sessionInfoRedis.getAccessToken(), sessionInfoRedis.getUserId())
                .userAgent(sessionInfoRedis.getUserAgent())
                .expirationTime(sessionInfoRedis.getExpirationTime())
                .build();
    }

    public Boolean isExpired(){
        return this.expirationTime.isBefore(LocalDateTime.now());
    }

    public void checkExpiration(){
        if(isExpired())
            throw new AccessedExpiredSessionTokenException();
    }



}
