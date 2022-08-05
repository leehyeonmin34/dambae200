package com.dambae200.dambae200.domain.notification.service.accessNotification;

import com.dambae200.dambae200.domain.access.domain.Access;
import com.dambae200.dambae200.domain.access.domain.AccessType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccessNotificationTypeFinder {
    // 데이터 -> 상황타입 추출 -> 알림타입 반환
    public List<AccessNotificationType> getNotificationType(AccessType prev, AccessType curr, Boolean byAdmin){
        AccessSituationType accessSituationType = AccessSituationType.findBy(prev, curr, byAdmin);
        return AccessNotificationType.findBy(accessSituationType);
    }


}
