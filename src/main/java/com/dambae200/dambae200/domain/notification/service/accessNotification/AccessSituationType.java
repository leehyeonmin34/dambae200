package com.dambae200.dambae200.domain.notification.service.accessNotification;

import com.dambae200.dambae200.domain.access.domain.AccessType;
import com.dambae200.dambae200.domain.access.exception.CannotFindAccessSituationTypeException;
import com.dambae200.dambae200.domain.notification.exception.CannotFindAccessNotificationType;
import org.springframework.data.domain.PageRequest;

public enum AccessSituationType {
    APPLIED, //신청됨
    APPLY_CANCELED, //신청 취소됨
    WITHDRAWAL, // 매장 탈퇴됨
    ACCESS_APPRVOED, // 요청 승인됨
    ACCESS_DENIED, // 요청 거절됨
    ADMIN_PROMOTED, // 관리자로 승급함
    ACCESS_REMOVED; // 접근권한 삭제됨

    public static AccessSituationType findBy(final AccessType prev, final AccessType curr, final Boolean byAdmin){
        // 매장 관리자에 의해 행해졌을 때
        if (byAdmin){
            switch ( prev ){
                case WAITING :
                    switch (curr){
                        case ACCESSIBLE:
                            return ACCESS_APPRVOED;
                        case INACCESSIBLE:
                            return ACCESS_DENIED;
                    }
                case ACCESSIBLE :
                    switch (curr){
                        case ADMIN:
                            return ADMIN_PROMOTED;
                        case INACCESSIBLE:
                            return ACCESS_REMOVED;
                    }
                default: throw new CannotFindAccessSituationTypeException(prev, curr, byAdmin);
            }
        } else {
            if((prev == null || prev.equals(AccessType.INACCESSIBLE)) && curr.equals(AccessType.WAITING))
                return APPLIED;
            else if (prev.equals(AccessType.WAITING) && curr.equals(AccessType.INACCESSIBLE))
                return APPLY_CANCELED;
            else if (prev.equals(AccessType.ACCESSIBLE) && curr.equals(AccessType.INACCESSIBLE))
                return WITHDRAWAL;
            else throw new CannotFindAccessSituationTypeException(prev, curr, byAdmin);
        }
    }

}
