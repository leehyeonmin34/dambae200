package com.dambae200.dambae200.domain.notification.service;

import com.dambae200.dambae200.domain.notification.domain.Notification;
import com.dambae200.dambae200.domain.notification.dto.NotificationDto;
import com.dambae200.dambae200.domain.notification.repository.NotificationRepository;
import com.dambae200.dambae200.global.common.RepoUtils;
import com.dambae200.dambae200.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationFindService {

    final NotificationRepository notificationRepository;
    final RepoUtils repoUtils;

    @Transactional(readOnly = true)
    public Page<NotificationDto.GetResponse> findByUserId(Long userId, Pageable pageable){
        Page<Notification> entities = notificationRepository.findByUserId(userId, pageable);
        return entities.map(NotificationDto.GetResponse::new);
    }

    @Transactional(readOnly = true)
    public boolean unreadExistByUserId(Long userId){
        return notificationRepository.existsByUserIdAndIsRead(userId, false);
    }

    @Transactional(readOnly = true)
    public NotificationDto.GetResponse findById(Long id) throws EntityNotFoundException {
        Notification entity = repoUtils.getOneElseThrowException(notificationRepository, id);
        return new NotificationDto.GetResponse(entity);
    }

}
