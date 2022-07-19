package com.dambae200.dambae200.domain.notification.service;

import com.dambae200.dambae200.domain.notification.domain.Notification;
import com.dambae200.dambae200.domain.notification.exception.CannotFindAccessNotificationType;
import com.dambae200.dambae200.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationSender{

    final NotificationRepository notificationRepository;

    public void send(List<Notification> notifications){
        notificationRepository.saveAll(notifications);
    }
    public void send(Notification notification){
        notificationRepository.save(notification);
    }
}
