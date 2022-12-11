package com.dambae200.dambae200.domain.user.exception;

import com.dambae200.dambae200.global.error.exception.EntityNotFoundException;
import com.dambae200.dambae200.global.error.exception.ErrorCode;

public class EmailNotFoundException extends EntityNotFoundException {

    public EmailNotFoundException(String email) {
        super(ErrorCode.EMAIL_NOT_FOUND, email + "에 대한 회원 정보가 존재하지 않습니다.");
    }
}
