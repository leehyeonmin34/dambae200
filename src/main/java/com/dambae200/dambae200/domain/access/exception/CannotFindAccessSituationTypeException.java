package com.dambae200.dambae200.domain.access.exception;

import com.dambae200.dambae200.domain.access.domain.AccessType;
import com.dambae200.dambae200.global.error.exception.ErrorCode;
import com.dambae200.dambae200.global.error.exception.UnhandledServerException;

public class CannotFindAccessSituationTypeException extends UnhandledServerException {

    public CannotFindAccessSituationTypeException(AccessType prev, AccessType curr, Boolean byAdmin){
        super(ErrorCode.CANNOT_FIND_ACCESS_SITUATION_TYPE
        , String.format("Access Situation을 찾을 수 없습니다. prev: %s, curr %s, byAdmin: %s", prev.getCode(), curr.getCode(), byAdmin));
    }

}
