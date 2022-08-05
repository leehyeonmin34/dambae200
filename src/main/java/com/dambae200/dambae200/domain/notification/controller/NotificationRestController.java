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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    // TODO 어떻게 보면 /user/{id}/notifications 인데 이렇게 하는 게 괜찮은가?
    @GetMapping("")
    public ResponseEntity<Page<NotificationDto.GetResponse>> getNotificationsByUserId(
            @RequestParam @NotNull String userId,
            @PageableDefault(size = 5, sort = "createdAt",  direction = Sort.Direction.DESC) Pageable pageable){
        Page<NotificationDto.GetResponse> response = notificationFindService.findByUserId(Long.valueOf(userId), pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/read")
    public ResponseEntity<NotificationDto.GetListResponse> markAsRead(@RequestBody NotificationDto.MarkAsReadRequest request) throws EntityNotFoundException {
        NotificationDto.GetListResponse response = notificationUpdateService.markAsReadNotifiations(request.getIdList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/read")
    public ResponseEntity<Boolean> unreadExistsByUserId(Long userId){
        Boolean response = (Boolean)notificationFindService.unreadExistByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteNotification(@PathVariable String id){
        DeleteResponse response = notificationUpdateService.deleteNotification(Long.valueOf(id));
        return ResponseEntity.ok(response);
    }

}
