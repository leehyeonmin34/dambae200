package com.dambae200.dambae200.domain.notification.service.accessNotification;

import com.dambae200.dambae200.domain.access.domain.Access;
import com.dambae200.dambae200.domain.access.domain.AccessType;
import com.dambae200.dambae200.domain.notification.domain.Notification;
import com.dambae200.dambae200.domain.notification.service.NotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class AccessNotificationGeneratorAndSender {

    private final AccessNotificationGenerator generator;
    private final NotificationSender sender;


    // 이 트랜잭션이 실패하더라도 상위 트랜잭션은 롤백되지 않음
    public void from(final AccessType prev, final Access access, final Boolean byAdmin){
        List<Notification> notifications = generator.generate(prev, access, byAdmin);
        sender.send(notifications);
    }

}
