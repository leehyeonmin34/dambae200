package com.dambae200.dambae200.domain.user.exception;

import com.dambae200.dambae200.global.error.exception.BusinessException;
import com.dambae200.dambae200.global.error.exception.DuplicatedEntityException;
import com.dambae200.dambae200.global.error.exception.ErrorCode;

public class DuplicatedEmailException extends DuplicatedEntityException {

    public DuplicatedEmailException() {
        super(ErrorCode.EMAIL_DUPLICATION, "중복된 이메일입니다.");
    }
}
