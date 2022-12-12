package com.dambae200.dambae200.domain.user.exception;

import com.dambae200.dambae200.global.error.exception.DuplicatedEntityException;
import com.dambae200.dambae200.global.error.exception.ErrorCode;

public class DuplicatedNicknameException extends DuplicatedEntityException {


    public DuplicatedNicknameException() {
        super(ErrorCode.NICKNAME_DUPLICATION, "중복된 닉네임입니다.");
    }
}
