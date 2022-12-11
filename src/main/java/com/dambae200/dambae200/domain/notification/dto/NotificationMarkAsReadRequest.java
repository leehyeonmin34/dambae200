package com.dambae200.dambae200.domain.notification.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class NotificationMarkAsReadRequest{
    private List<Long> idList;
}