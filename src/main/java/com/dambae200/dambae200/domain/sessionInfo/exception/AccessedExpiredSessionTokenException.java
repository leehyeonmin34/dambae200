package com.dambae200.dambae200.domain.sessionInfo.exception;

import com.dambae200.dambae200.global.error.exception.BusinessException;
import com.dambae200.dambae200.global.error.exception.ErrorCode;

public class AccessedExpiredSessionTokenException extends BusinessException {

    public AccessedExpiredSessionTokenException() {
        super(ErrorCode.ACCESSED_EXPIRED_SESSION_TOKEN);
    }


}
