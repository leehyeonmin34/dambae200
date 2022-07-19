package com.dambae200.dambae200.domain.notification.service.storeNotification;

import com.dambae200.dambae200.domain.access.domain.Access;
import com.dambae200.dambae200.domain.access.domain.AccessType;
import com.dambae200.dambae200.domain.access.repository.AccessRepository;
import com.dambae200.dambae200.domain.notification.domain.Notification;
import com.dambae200.dambae200.domain.store.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class StoreNotificationGenerator {

    final AccessRepository accessRepository;

    public List<Notification> generate(Store store, StoreNotificationType type, String oldName){
        // 해당 매장의 접근 권한자들의 아이디를 로드
        List<Access> accessList = accessRepository.findAllByStoreId(store.getId());
        List<Long> recieverIdList = accessList.stream()
                .filter(item -> item.getAccessType().equals(AccessType.ACCESSIBLE))
                .map(item -> item.getId())
                .collect(Collectors.toList());

        // 해당 사용자들에게 전달할 알림 생성
        String title = type.getTitleFrom(store.getName());
        String content = type.getTitleFrom(store.getName(), oldName);
        return recieverIdList.stream().map(id ->
                Notification.builder()
                    .content(content)
                    .title(title)
                    .userId(id)
                    .build()
        ).collect(Collectors.toList());
    }

}
