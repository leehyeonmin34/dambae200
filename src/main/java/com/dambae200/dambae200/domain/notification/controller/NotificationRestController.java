package com.dambae200.dambae200.domain.notification.controller;


import com.dambae200.dambae200.domain.notification.dto.NotificationGetListResponse;
import com.dambae200.dambae200.domain.notification.dto.NotificationGetResponse;
import com.dambae200.dambae200.domain.notification.dto.NotificationMarkAsReadRequest;
import com.dambae200.dambae200.domain.notification.service.NotificationFindService;
import com.dambae200.dambae200.domain.notification.service.NotificationUpdateService;
import com.dambae200.dambae200.global.common.dto.DeleteResponse;
import com.dambae200.dambae200.global.common.dto.StandardResponse;
import com.dambae200.dambae200.global.error.exception.EntityNotFoundException;
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
    public ResponseEntity<StandardResponse<NotificationGetResponse>> getNotificationById(@PathVariable String id){
        NotificationGetResponse response = notificationFindService.findById(Long.valueOf(id));
        return StandardResponse.ofOk(response);
    }

    // TODO 어떻게 보면 /user/{id}/notifications 인데 이렇게 하는 게 괜찮은가?
    @GetMapping("")
    public ResponseEntity<StandardResponse<Page<NotificationGetResponse>>> getNotificationsByUserId(
            @RequestParam @NotNull String userId,
            @PageableDefault(size = 5, sort = "createdAt",  direction = Sort.Direction.DESC) Pageable pageable){
        Page<NotificationGetResponse> response = notificationFindService.findByUserId(Long.valueOf(userId), pageable);
        return StandardResponse.ofOk(response);
    }

    @PutMapping("/read")
    public ResponseEntity<StandardResponse<NotificationGetListResponse>> markAsRead(@RequestBody NotificationMarkAsReadRequest request) throws EntityNotFoundException {
        NotificationGetListResponse response = notificationUpdateService.markAsReadNotifiations(request.getIdList());
        return StandardResponse.ofOk(response);
    }

    @GetMapping("/read")
    public ResponseEntity<StandardResponse<Boolean>> unreadExistsByUserId(Long userId){
        Boolean response = (Boolean)notificationFindService.unreadExistByUserId(userId);
        return StandardResponse.ofOk(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse<DeleteResponse>> deleteNotification(@PathVariable String id){
        DeleteResponse response = notificationUpdateService.deleteNotification(Long.valueOf(id));
        return StandardResponse.ofOk(response);
    }

}
