package com.dambae200.dambae200.domain.notification.service.accessNotification;

import com.dambae200.dambae200.domain.access.domain.Access;
import com.dambae200.dambae200.domain.access.domain.AccessType;
import com.dambae200.dambae200.domain.notification.domain.Notification;
import com.dambae200.dambae200.domain.notification.service.NotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class AccessNotificationGeneratorAndSender {

    final AccessNotificationGenerator generator;
    final NotificationSender sender;

    public void from(AccessType prev, Access access, Boolean byAdmin){
        List<Notification> notifications = generator.generate(prev, access, byAdmin);
        sender.send(notifications);
    }

}
