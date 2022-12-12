package com.dambae200.dambae200.domain.notification.domain;

import com.dambae200.dambae200.global.common.dto.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Getter
@Table(name = "notification")
public class Notification extends BaseEntity {
    private Boolean isRead = false;

    @JoinColumn(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Builder
    public Notification(Long userId, String title, String content){
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

    public void markAsRead(){
        this.isRead = true;
    }

}
