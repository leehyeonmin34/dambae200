package com.dambae200.dambae200.domain.notification.service;

import com.dambae200.dambae200.domain.notification.domain.Notification;
import com.dambae200.dambae200.domain.notification.dto.NotificationDto;
import com.dambae200.dambae200.domain.notification.repository.NotificationRepository;
import com.dambae200.dambae200.global.common.DeleteResponse;
import com.dambae200.dambae200.global.common.RepoUtils;
import com.dambae200.dambae200.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Delete;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationUpdateService {

    final NotificationRepository notificationRepository;
    final RepoUtils repoUtils;

    public NotificationDto.GetResponse readNotifiation(Long id) throws EntityNotFoundException {

        // 유효성검사, 로드
        Notification notification = repoUtils.getOneElseThrowException(notificationRepository, id);

        // 처리
        notification.markAsRead();
        Notification saved = notificationRepository.save(notification);

        // 리턴
        return new NotificationDto.GetResponse(saved);
    }

    public DeleteResponse deleteNotification(Long id) throws EntityNotFoundException {
        // 유효성검사, 처리
        repoUtils.deleteOneElseThrowException(notificationRepository, id);

        // 리턴
        return new DeleteResponse("notification", id);
    }

}
