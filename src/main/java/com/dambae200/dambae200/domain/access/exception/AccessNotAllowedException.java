package com.dambae200.dambae200.domain.access.exception;

import com.dambae200.dambae200.domain.access.domain.AccessType;
import com.dambae200.dambae200.global.error.exception.BusinessException;
import com.dambae200.dambae200.global.error.exception.ErrorCode;

public class AccessNotAllowedException extends BusinessException {

    public AccessNotAllowedException(Long userId, Long storeId, AccessType accessType) {
        super(ErrorCode.ACCESS_NOT_ALLOWED, String.format("userId %s는 storeId %s에 대한 '%s' 접근 권한이 없습니다.", userId, storeId, accessType.getDesc()));
    }
}
