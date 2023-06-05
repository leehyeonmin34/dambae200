package com.dambae200.dambae200.domain.notification.dto;

import com.dambae200.dambae200.domain.notification.domain.Notification;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NotificationGetResponse{
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Boolean isRead;

    public NotificationGetResponse(Notification notification){
        this.id = notification.getId();
        this.title = notification.getTitle();
        this.content = notification.getContent();
        this.createdAt = notification.getCreatedAt();
        this.isRead = notification.getIsRead();
    }
}