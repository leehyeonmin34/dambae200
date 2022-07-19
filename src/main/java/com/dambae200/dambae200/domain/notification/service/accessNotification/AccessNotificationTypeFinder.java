package com.dambae200.dambae200.domain.notification.service.accessNotification;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccessNotificationTypeFinder {
    public List<AccessNotificationType> getNotificationTypeBySituationType(AccessSituationType type){
        // ACCESSIBLE, INACCESSIBLE, ADMIN, WAITING
        switch (type){
            case APPLIED:
                return List.of(AccessNotificationType.ACCESS_APPLY, AccessNotificationType.APPLLICATION_CAME);
            case APPLY_CANCELED:
                return List.of(AccessNotificationType.APPLICATION_CANCELD);
            case WITHDRAWAL:
                return List.of(AccessNotificationType.USER_WITHDRAWAL);
            case ACCESS_APPRVOED:
                return List.of(AccessNotificationType.ACCESS_APPROVED);
            case ACCESS_DENIED:
                return List.of(AccessNotificationType.ACCESS_DENIED);
            case ADMIN_PROMOTED:
                return List.of(AccessNotificationType.ACCESS_ADMIN_PROMOTION);
            case ACCESS_REMOVED:
                return List.of(AccessNotificationType.ACCESS_DELETED);
        }
        return null;
    }
}
