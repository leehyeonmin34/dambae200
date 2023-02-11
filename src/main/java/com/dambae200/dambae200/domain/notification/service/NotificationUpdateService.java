package com.dambae200.dambae200.domain.notification.service;

import com.dambae200.dambae200.domain.notification.domain.Notification;
import com.dambae200.dambae200.domain.notification.dto.NotificationGetListResponse;
import com.dambae200.dambae200.domain.notification.repository.NotificationRepository;
import com.dambae200.dambae200.global.common.dto.DeleteResponse;
import com.dambae200.dambae200.global.common.service.RepoUtils;
import com.dambae200.dambae200.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationUpdateService {

    private final NotificationRepository notificationRepository;
    private final RepoUtils repoUtils;

    public NotificationGetListResponse markAsReadNotifiations(final List<Long> idList) {

        final List<Notification> notifications = notificationRepository.findAllById(idList).stream().map(item -> {
            item.markAsRead();
            notificationRepository.save(item);
            return item;
        }).collect(Collectors.toList());

        return new NotificationGetListResponse(notifications);
    }

    public DeleteResponse deleteNotification(final Long id) throws EntityNotFoundException {
        // 유효성검사, 처리
        repoUtils.deleteOneElseThrowException(notificationRepository, id);

        // 리턴
        return new DeleteResponse("notification", id);
    }

}
