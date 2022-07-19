package com.dambae200.dambae200.domain.notification.service.storeNotification;

import com.dambae200.dambae200.domain.notification.domain.Notification;
import com.dambae200.dambae200.domain.notification.service.NotificationSender;
import com.dambae200.dambae200.domain.store.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class StoreNotificationGeneratorAndSender {

    final StoreNotificationGenerator generator;
    final NotificationSender sender;

    public void generateAndSend(Store store, StoreNotificationType type, String oldName){
        List<Notification> notifications = generator.generate(store, type, oldName);
        sender.send(notifications);
    }

    public void generateAndSend(Store store, StoreNotificationType type){
        List<Notification> notifications = generator.generate(store, type, null);
        sender.send(notifications);
    }
}
