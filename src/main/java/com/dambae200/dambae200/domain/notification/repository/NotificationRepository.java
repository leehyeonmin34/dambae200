package com.dambae200.dambae200.domain.notification.repository;

import com.dambae200.dambae200.domain.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserId(Long id);
    void deleteAllByUserId(Long id);
}
