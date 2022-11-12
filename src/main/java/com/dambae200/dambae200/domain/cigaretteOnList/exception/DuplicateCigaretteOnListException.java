package com.dambae200.dambae200.domain.cigaretteOnList.exception;

import com.dambae200.dambae200.global.error.exception.BusinessException;
import com.dambae200.dambae200.global.error.exception.ErrorCode;

public class DuplicateCigaretteOnListException extends BusinessException {

    public DuplicateCigaretteOnListException(Long cigaretteListId, Long cigaretteId) {
        super(ErrorCode.DUPLICATE_CIGARETTE_ON_LIST, String.format("cigaretteListId %s, cigaretteId %s 에 대한 cigaretteOnList가 이미 존재합니다.", cigaretteListId, cigaretteId));
    }
}
