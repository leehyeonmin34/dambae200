package com.dambae200.dambae200.domain.user.exception;

import com.dambae200.dambae200.global.error.exception.BusinessException;
import com.dambae200.dambae200.global.error.exception.ErrorCode;

public class LoginInfoNotMatched extends BusinessException {
    public LoginInfoNotMatched() {
        super(ErrorCode.LOGIN_INFO_NOT_MATCHED, "일치하는 로그인 정보가 없습니다.");
    }
}


