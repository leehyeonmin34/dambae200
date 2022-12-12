package com.dambae200.dambae200.domain.user.controller;

import com.dambae200.dambae200.domain.access.dto.AccessGetStoreListResponse;
import com.dambae200.dambae200.domain.access.service.AccessService;
import com.dambae200.dambae200.domain.notification.service.NotificationFindService;
import com.dambae200.dambae200.domain.user.dto.*;
import com.dambae200.dambae200.domain.user.service.UserFindService;
import com.dambae200.dambae200.domain.user.service.UserUpdateService;
import com.dambae200.dambae200.global.common.dto.DeleteResponse;
import com.dambae200.dambae200.global.common.dto.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    final UserUpdateService userUpdateService;
    final UserFindService userFindService;
    final AccessService accessService;
    final NotificationFindService notificationFindService;

    @GetMapping("/exists_by_email")
    public ResponseEntity<StandardResponse<Boolean>> existsByEmail(@RequestParam @NotBlank String email){
        Boolean response = userFindService.existsByEmail(email);
        return StandardResponse.ofOk(response);
    }

    @GetMapping("/exists_by_nickname")
    public ResponseEntity<StandardResponse<Boolean>> existsByNickname(@RequestParam @NotBlank String nickname){
        Boolean response = userFindService.existsByNickname(nickname);
        return StandardResponse.ofOk(response);
    }

    @GetMapping("/{id}/home")
    public ResponseEntity<StandardResponse<UserHomeDto>> findHomeInfoById(@PathVariable String id){
        boolean newNoti = notificationFindService.unreadExistByUserId(Long.valueOf(id));
        AccessGetStoreListResponse stores = accessService.findAllByUserId(Long.valueOf(id));
        UserHomeDto response = UserHomeDto.builder()
                .myStores(stores)
                .newNotification(newNoti)
                .build();
        System.out.println(response);
        return StandardResponse.ofOk(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse<UserGetResponse>> findById(@PathVariable String id){
        UserGetResponse response = userFindService.findById(Long.valueOf(id));
        return StandardResponse.ofOk(response);
    }

    @PostMapping("")
    public ResponseEntity<StandardResponse<UserGetResponse>> addUser(@RequestBody @Valid UserAddRequest request){
        UserGetResponse response = userUpdateService.addUser(request);
        return StandardResponse.ofOk(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StandardResponse<UserGetResponse>> updateUser(@PathVariable String id, @RequestBody @Valid UserUpdateRequest request){
        UserGetResponse response = userUpdateService.updateUser(Long.valueOf(id), request);
        return StandardResponse.ofOk(response);
    }


    @PutMapping("/{id}/change_pw")
    public ResponseEntity<StandardResponse<UserGetResponse>> changeUserPassword(@PathVariable String id, @RequestBody @Valid UserChangePasswordRequest request){
        UserGetResponse response = userUpdateService.changeUserPassword(Long.valueOf(id), request);
        return StandardResponse.ofOk(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse<DeleteResponse>> deleteUser(@PathVariable String id, @RequestParam String pw){
        DeleteResponse response = userUpdateService.deleteUser(Long.valueOf(id), pw);
        return StandardResponse.ofOk(response);
    }




}
