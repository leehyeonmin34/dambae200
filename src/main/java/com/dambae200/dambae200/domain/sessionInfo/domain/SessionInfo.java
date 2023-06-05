package com.dambae200.dambae200.domain.sessionInfo.domain;

import com.dambae200.dambae200.domain.sessionInfo.exception.AccessedExpiredSessionTokenException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SessionInfo {

    @Id
    @Column(name = "access_token", nullable = false, updatable = false, unique = true)
    private String accessToken;

    @Column(name = "user_id", nullable = false, updatable = false, unique = true)
    @Getter
    private Long userId;

    @Column(name = "user_agent", nullable = true, updatable = true, unique = false)
    @Getter
    private String userAgent;

    @Column(name = "expiration_time", nullable = true, updatable = true, unique = false)
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

        public Builder userAgent(String userAgent){
            this.userAgent = userAgent;
            return this;
        }

        public Builder expirationTime(LocalDateTime expirationTime){
            this.expirationTime = expirationTime;
            return this;
        }

        public SessionInfo build(){
            return new SessionInfo(this);
        }

    }

    private SessionInfo(Builder builder){
        this.accessToken = builder.getAccessToken();
        this.userId = builder.getUserId();
        this.userAgent = builder.getUserAgent();
        this.expirationTime = builder.getExpirationTime();
    }

    public Boolean isExpired(){
        return this.expirationTime.isBefore(LocalDateTime.now());
    }


    public void checkExpiration(){
        if(isExpired())
            throw new AccessedExpiredSessionTokenException();
    }



}
