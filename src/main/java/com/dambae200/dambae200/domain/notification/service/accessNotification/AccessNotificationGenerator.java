package com.dambae200.dambae200.domain.notification.service.accessNotification;

import com.dambae200.dambae200.domain.access.domain.Access;
import com.dambae200.dambae200.domain.access.domain.AccessType;
import com.dambae200.dambae200.domain.access.repository.AccessRepository;
import com.dambae200.dambae200.domain.notification.domain.Notification;
import com.dambae200.dambae200.domain.notification.exception.CannotFindAccessNotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class AccessNotificationGenerator {

    final AccessNotificationTypeFinder accessNotificationTypeFinder;
    final AccessRepository accessRepository;

    // 데이터 기반해 알림 타입 선택
    private List<AccessNotificationType> getNotificationType(AccessType prev, AccessType curr, Boolean byAdmin) throws CannotFindAccessNotificationType {
        return accessNotificationTypeFinder.getNotificationType(prev, curr, byAdmin);
    }

    // 알림 엔티티 생성
    public List<Notification> generate(AccessType prev, Access access, Boolean byAdmin) throws CannotFindAccessNotificationType{

        // 알림타입(템플릿) 선택
        List<AccessNotificationType> notificationTypes = getNotificationType(prev, access.getAccessType(), byAdmin);

        // 알림 엔티티에 넣을 데이터 추출
        String storeName = access.getStore().getFullname();
        Long userId = access.getUser().getId();
        String userNickname = access.getUser().getNickname();
        // TODO admin ID를 이렇게 구하는 게 맞을까?
        Long adminId = accessRepository.findAllByStoreId(access.getStore().getId())
                .stream().filter(item -> item.getAccessType().equals(AccessType.ADMIN)).findAny().get().getId();

        // 각 알림 타입에 맞는 알림 생성
        List<Notification> notifications = notificationTypes.stream().map(item -> {
            Long receiverId = item.getToAdmin() ? adminId : userId;
            String title = item.getTitleFrom(storeName, userNickname);
            String content = item.getContentFrom(storeName, userNickname);
            Notification notification = Notification.builder()
                    .title(title)
                    .content(content)
                    .userId(receiverId)
                    .build();
            return notification;
        }).collect(Collectors.toList());

        return notifications;

    }

}
