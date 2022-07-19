package com.dambae200.dambae200.domain.user.exception;

import com.dambae200.dambae200.global.error.exception.BusinessException;
import com.dambae200.dambae200.global.error.exception.ErrorCode;

import java.nio.Buffer;

public class LoginInputInvalidException extends BusinessException {

    public LoginInputInvalidException() {
        super(ErrorCode.LOGIN_INPUT_INVALID, "잘못된 로그인 정보입니다.");
    }
}
