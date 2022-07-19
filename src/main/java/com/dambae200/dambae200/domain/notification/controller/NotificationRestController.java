package com.dambae200.dambae200.domain.notification.controller;


import com.dambae200.dambae200.domain.access.dto.AccessDto;
import com.dambae200.dambae200.domain.notification.domain.Notification;
import com.dambae200.dambae200.domain.notification.dto.NotificationDto;
import com.dambae200.dambae200.domain.notification.service.NotificationFindService;
import com.dambae200.dambae200.domain.notification.service.NotificationUpdateService;
import com.dambae200.dambae200.global.common.DeleteResponse;
import com.dambae200.dambae200.global.error.exception.EntityNotFoundException;
import io.swagger.models.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationRestController {

    final NotificationUpdateService notificationUpdateService;
    final NotificationFindService notificationFindService;

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto.GetResponse> getNotificationById(@PathVariable String id){
        NotificationDto.GetResponse response = notificationFindService.findById(Long.valueOf(id));
        return ResponseEntity.ok(response);
    }

    // TODO page로 받아와야 할 것 같다.
    // TODO 어떻게 보면 /user/{id}/notifications 인데 이렇게 하는 게 괜찮은가?
    @GetMapping("")
    public ResponseEntity<NotificationDto.GetListResponse> getNotificationsByUserId(@RequestParam @NotNull String userId){
        NotificationDto.GetListResponse response = notificationFindService.findAllByUserId(Long.valueOf(userId));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationDto.GetResponse> markAsRead(@PathVariable String id) throws EntityNotFoundException {
        NotificationDto.GetResponse response = notificationUpdateService.readNotifiation(Long.valueOf(id));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteNotification(@PathVariable String id){
        DeleteResponse response = notificationUpdateService.deleteNotification(Long.valueOf(id));
        return ResponseEntity.ok(response);
    }

}
