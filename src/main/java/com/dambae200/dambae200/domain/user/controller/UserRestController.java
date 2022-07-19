package com.dambae200.dambae200.domain.user.controller;

import com.dambae200.dambae200.domain.user.dto.UserDto;
import com.dambae200.dambae200.domain.user.service.UserFindService;
import com.dambae200.dambae200.domain.user.service.UserUpdateService;
import com.dambae200.dambae200.global.common.DeleteResponse;
import io.swagger.models.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    final UserUpdateService userUpdateService;
    final UserFindService userFindService;

    @GetMapping("/existsByEmail")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam @NotBlank String email){
        Boolean response = userFindService.existsByEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/existsByNickname")
    public ResponseEntity<Boolean> existsByNickname(@RequestParam @NotBlank String nickname){
        Boolean response = userFindService.existsByNickname(nickname);
        return ResponseEntity.ok(response);
    }

    // TODO Password 받는 법
    @PostMapping("")
    public ResponseEntity<UserDto.GetResponse> addUser(@RequestBody @Valid UserDto.AddRequest request){
        UserDto.GetResponse response = userUpdateService.addUser(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto.GetResponse> updateUser(@PathVariable String id, @RequestBody @Valid UserDto.UpdateRequest request){
        UserDto.GetResponse response = userUpdateService.updateUser(Long.valueOf(id), request);
        return ResponseEntity.ok(response);
    }


    // TODO Password 받는 법
    @PutMapping("/{id}/changePw")
    public ResponseEntity<UserDto.GetResponse> changeUserPassword(@PathVariable String id, @RequestBody @Valid UserDto.ChangePasswordRequest request){
        UserDto.GetResponse response = userUpdateService.changeUserPassword(Long.valueOf(id), request);
        return ResponseEntity.ok(response);
    }

    // TODO password 받는 법
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteUser(@PathVariable String id, @RequestParam String pw){
        DeleteResponse response = userUpdateService.deleteUser(Long.valueOf(id), pw);
        return ResponseEntity.ok(response);
    }



}
