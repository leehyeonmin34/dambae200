package com.dambae200.dambae200.domain.sessionInfo.exception;

import com.dambae200.dambae200.global.error.exception.BusinessException;
import com.dambae200.dambae200.global.error.exception.ErrorCode;

public class SessionInfoNotExistsException extends BusinessException {

    public SessionInfoNotExistsException() {
        super(ErrorCode.SESSION_INFO_NOT_EXISTS);
    }

}
