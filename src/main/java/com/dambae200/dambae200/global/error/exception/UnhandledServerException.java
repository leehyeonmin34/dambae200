package com.dambae200.dambae200.global.error.exception;

public class UnhandledServerException extends BusinessException{

    public UnhandledServerException(String msg){
        super(ErrorCode.INTERNAL_SERVER_ERROR, msg);
    }

    public UnhandledServerException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnhandledServerException(ErrorCode errorCode, String msg) {
        super(errorCode, msg);
    }
}
