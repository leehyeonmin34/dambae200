package com.dambae200.dambae200.domain.access.exception;

import com.dambae200.dambae200.global.error.exception.BusinessException;
import com.dambae200.dambae200.global.error.exception.ErrorCode;

public class DuplicateAccessApply extends BusinessException {
    public DuplicateAccessApply(Long userId, Long storeId){
        super(ErrorCode.DUPLICATED_ACCESS_APPLY, String.format("userId %s, storeId %s에 대한 access가 이미 존재합니다.", userId, storeId));

    }
}
