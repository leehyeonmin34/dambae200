package com.dambae200.dambae200.global.error.exception;

public class DuplicatedEntityException extends BusinessException{

    public DuplicatedEntityException(){
        super(ErrorCode.DUPLICATED_ENTITY);
    }

    protected DuplicatedEntityException(ErrorCode errorCode) {
        super(errorCode);
    }

    protected DuplicatedEntityException(ErrorCode errorCode, String msg) {
        super(errorCode, msg);

    }
}
