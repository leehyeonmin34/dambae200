package com.dambae200.dambae200.global.error.exception;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String entity, String id){
        super(ErrorCode.ENTITY_NOT_FOUND, String.format("id %d에 해당하는 %s entity를 찾을 수 없습니다.", id, entity));
    }

    public EntityNotFoundException(String msg){
        super(ErrorCode.ENTITY_NOT_FOUND, msg);
    }

    protected EntityNotFoundException(ErrorCode errorCode, String msg){
        super(errorCode, msg);
    }

    protected EntityNotFoundException(ErrorCode errorCode){
        super(errorCode);
    }
}
