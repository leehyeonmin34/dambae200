package com.dambae200.dambae200.global.error.exception;

public class InvalidEnumCodeException extends BusinessException{

    public InvalidEnumCodeException(ErrorCode errorCode, String field, String code) {
        super(errorCode, String.format("잘못된 %s 코드입니다. code : %s", field, code));
    }
}
