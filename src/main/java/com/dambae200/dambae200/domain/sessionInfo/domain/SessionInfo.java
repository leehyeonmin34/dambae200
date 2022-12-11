package com.dambae200.dambae200.domain.sessionInfo.domain;

import com.dambae200.dambae200.domain.sessionInfo.exception.AccessedExpiredSessionTokenException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
public class SessionInfo implements Serializable {

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
//        return false;
        return this.expirationTime.isBefore(LocalDateTime.now());
    }


    public void checkExpiration(){
        if(isExpired())
            throw new AccessedExpiredSessionTokenException();
    }



}
