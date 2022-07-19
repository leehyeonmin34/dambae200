package com.dambae200.dambae200.domain.user.exception;

import com.dambae200.dambae200.global.error.exception.BusinessException;
import com.dambae200.dambae200.global.error.exception.ErrorCode;

public class EmailDuplicationException extends BusinessException {

    public EmailDuplicationException() {
        super(ErrorCode.EMAIL_DUPLICATION, "중복된 이메일입니다.");
    }
}
