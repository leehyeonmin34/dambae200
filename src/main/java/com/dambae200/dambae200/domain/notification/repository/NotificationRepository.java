package com.dambae200.dambae200.domain.notification.repository;

import com.dambae200.dambae200.domain.notification.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByUserIdLessThanId(Long userId, Long id, Pageable pageable);
    void deleteAllByUserId(Long id);
    boolean existsByUserIdAndIsRead(Long userId, boolean isRead);
}
