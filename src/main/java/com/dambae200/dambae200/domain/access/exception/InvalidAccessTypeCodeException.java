package com.dambae200.dambae200.domain.access.exception;

import com.dambae200.dambae200.global.error.exception.ErrorCode;
import com.dambae200.dambae200.global.error.exception.InvalidEnumCodeException;

public class InvalidAccessTypeCodeException extends InvalidEnumCodeException {
    public InvalidAccessTypeCodeException(String code) {
        super(ErrorCode.INVALID_ACCESS_TYPE_CODE, "access", code);
    }
}
