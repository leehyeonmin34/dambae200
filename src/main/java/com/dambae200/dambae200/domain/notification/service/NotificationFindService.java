package com.dambae200.dambae200.domain.notification.service;

import com.dambae200.dambae200.domain.notification.domain.Notification;
import com.dambae200.dambae200.domain.notification.dto.NotificationDto;
import com.dambae200.dambae200.domain.notification.repository.NotificationRepository;
import com.dambae200.dambae200.global.common.RepoUtils;
import com.dambae200.dambae200.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationFindService {

    final NotificationRepository notificationRepository;
    final RepoUtils repoUtils;

    public NotificationDto.GetListResponse findAllByUserId(Long userId){
        List<Notification> entities = notificationRepository.findAllByUserId(userId);
        return new NotificationDto.GetListResponse(entities);
    }

    public NotificationDto.GetResponse findById(Long id) throws EntityNotFoundException {
        Notification entity = repoUtils.getOneElseThrowException(notificationRepository, id);
        return new NotificationDto.GetResponse(entity);
    }

}
