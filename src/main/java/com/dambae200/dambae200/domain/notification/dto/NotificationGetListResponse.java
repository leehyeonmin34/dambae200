package com.dambae200.dambae200.domain.notification.dto;

import com.dambae200.dambae200.domain.notification.domain.Notification;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class NotificationGetListResponse{
    private List<NotificationGetResponse> notifications;
    private int total;

    public NotificationGetListResponse(List<Notification> notifications){
        this.notifications = notifications.stream()
                .map(NotificationGetResponse::new)
                .collect(Collectors.toList());
        this.total = this.notifications.size();
    }


}
