package com.dambae200.dambae200.domain.notification.dto;

import com.dambae200.dambae200.domain.notification.domain.Notification;
import com.dambae200.dambae200.domain.store.dto.StoreDto;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationDto {

    @Getter
    public static class GetResponse{
        private Long id;
        private String title;
        private String content;
        private LocalDateTime createdAt;
        private Boolean isRead;

        public GetResponse(Notification notification){
            this.id = notification.getId();
            this.title = notification.getTitle();
            this.content = notification.getContent();
            this.createdAt = notification.getCreatedAt();
            this.isRead = notification.getIsRead();
        }
    }

    @Getter
    public static class GetListResponse{
        private List<GetResponse> notifications;
        private int total;

        public GetListResponse(List<Notification> notifications){
            this.notifications = notifications.stream()
                    .map(GetResponse::new)
                    .collect(Collectors.toList());
            this.total = this.notifications.size();
        }


    }

}
