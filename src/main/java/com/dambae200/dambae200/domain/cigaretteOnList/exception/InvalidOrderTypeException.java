package com.dambae200.dambae200.domain.cigaretteOnList.exception;

import com.dambae200.dambae200.global.error.exception.ErrorCode;
import com.dambae200.dambae200.global.error.exception.InvalidEnumCodeException;

public class InvalidOrderTypeException extends InvalidEnumCodeException {

    public InvalidOrderTypeException(String code) {
        super(ErrorCode.INVALID_ORDER_TYPE_EXCEPTION, "OrderType", code);
    }
}
