package com.dambae200.dambae200.domain.notification.service;

import com.dambae200.dambae200.domain.notification.domain.Notification;
import com.dambae200.dambae200.domain.notification.exception.CannotFindAccessNotificationType;
import com.dambae200.dambae200.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationSender{

    private final NotificationRepository notificationRepository;

    public void send(final List<Notification> notifications){
        notificationRepository.saveAll(notifications);
    }
    public void send(final Notification notification){
        notificationRepository.save(notification);
    }
}
