package com.dambae200.dambae200.domain.access.exception;

import com.dambae200.dambae200.global.error.exception.DuplicatedEntityException;
import com.dambae200.dambae200.global.error.exception.ErrorCode;

public class DuplicatedAccessApply extends DuplicatedEntityException {
    public DuplicatedAccessApply(Long userId, Long storeId){
        super(ErrorCode.DUPLICATED_ACCESS_APPLY, String.format("userId %s, storeId %s에 대한 access가 이미 존재합니다.", userId, storeId));

    }
}
