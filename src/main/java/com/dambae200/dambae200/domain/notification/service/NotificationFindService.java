package com.dambae200.dambae200.domain.notification.service;

import com.dambae200.dambae200.domain.notification.domain.Notification;
import com.dambae200.dambae200.domain.notification.dto.NotificationGetResponse;
import com.dambae200.dambae200.domain.notification.repository.NotificationRepository;
import com.dambae200.dambae200.global.common.RepoUtils;
import com.dambae200.dambae200.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationFindService {

    final NotificationRepository notificationRepository;
    final RepoUtils repoUtils;

    @Transactional(readOnly = true)
    public Page<NotificationGetResponse> findByUserId(Long userId, Pageable pageable){
        Page<Notification> entities = notificationRepository.findByUserId(userId, pageable);
        return entities.map(NotificationGetResponse::new);
    }

    @Transactional(readOnly = true)
    public boolean unreadExistByUserId(Long userId){
        return notificationRepository.existsByUserIdAndIsRead(userId, false);
    }

    @Transactional(readOnly = true)
    public NotificationGetResponse findById(Long id) throws EntityNotFoundException {
        Notification entity = repoUtils.getOneElseThrowException(notificationRepository, id);
        return new NotificationGetResponse(entity);
    }

}
