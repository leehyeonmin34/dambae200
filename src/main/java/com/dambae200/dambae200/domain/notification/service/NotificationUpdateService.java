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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationUpdateService {

    final NotificationRepository notificationRepository;
    final RepoUtils repoUtils;

    @Transactional
    public NotificationDto.GetListResponse markAsReadNotifiations(List<Long> idList) {

        List<Notification> notifications = notificationRepository.findAllById(idList).stream().map(item -> {
//            System.out.println("notification id " + item.getId() + "를 읽겠습니다.");
            item.markAsRead();
            notificationRepository.save(item);
            return item;
        }).collect(Collectors.toList());

        return new NotificationDto.GetListResponse(notifications);
    }

    @Transactional
    public DeleteResponse deleteNotification(Long id) throws EntityNotFoundException {
        // 유효성검사, 처리
        repoUtils.deleteOneElseThrowException(notificationRepository, id);

        // 리턴
        return new DeleteResponse("notification", id);
    }

}
