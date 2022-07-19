package com.dambae200.dambae200.global.error.exception;

public class InvalidValueException extends BusinessException {
    public InvalidValueException(String msg){
        super(ErrorCode.INVALID_INPUT_VALUE, msg);
    }
}
