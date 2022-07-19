package com.dambae200.dambae200.global.error.exception;

public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(String msg){
        super(ErrorCode.ENTITY_NOT_FOUND, msg);
    }
}
