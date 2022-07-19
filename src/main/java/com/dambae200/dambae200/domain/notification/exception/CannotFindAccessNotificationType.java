package com.dambae200.dambae200.domain.notification.exception;

import com.dambae200.dambae200.domain.access.domain.AccessType;
import com.dambae200.dambae200.global.error.exception.BusinessException;
import com.dambae200.dambae200.global.error.exception.ErrorCode;

public class CannotFindAccessNotificationType extends BusinessException {
    public CannotFindAccessNotificationType(AccessType prev, AccessType curr) {
        super(ErrorCode.CANNOT_FIND_ACCESS_NOTIFCATION_TYPE,
                String.format("접근 권한 알림 타입을 찾을 수 없습니다. 이전 타입 : %s, 현재 타입 : %s"
                        , prev.getDesc(), curr.getDesc()
                )
        );
    }

}
