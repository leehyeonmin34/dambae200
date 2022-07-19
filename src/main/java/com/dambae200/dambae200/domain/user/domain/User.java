package com.dambae200.dambae200.domain.user.domain;

import com.dambae200.dambae200.domain.user.exception.LoginInputInvalidException;
import com.dambae200.dambae200.global.common.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Getter
@Table(name = "user")
public class User extends BaseEntity {

    @Column(name = "email", nullable = false, updatable = false, unique = true)
    private String email;

    @Column(name = "pw", nullable = false)
    private String pw;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Builder
    public User(String email, String pw, String nickname){
        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
    }

    public void changePw(String oldPw, String newPw){
        authenticate(oldPw);
        this.pw = newPw;
    }

    public void changeNickname(String nickName){
        this.nickname = nickName;
    }

    public Boolean authenticate(String pw){
        if (this.pw.equals(pw))
            return true;
        else throw new LoginInputInvalidException();
    }


}
