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

    @Column(name = "is_read", nullable = false, updatable = true, unique = false)
    private Boolean isRead = false;

    @JoinColumn(name = "userId", nullable = false, updatable = false, unique = false)
    private Long userId;

    @Column(name = "title", nullable = false, updatable = false, unique = false)
    private String title;

    @Column(name = "content", nullable = false, updatable = false, unique = false)
    private String content;

    public Notification(Long userId, String title, String content){
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

    public void markAsRead(){
        this.isRead = true;
    }

}
