package com.dambae200.dambae200.domain.access.controller;


import com.dambae200.dambae200.domain.access.dto.AccessDto;
import com.dambae200.dambae200.domain.access.service.AccessService;
import com.dambae200.dambae200.domain.user.dto.UserDto;
import com.dambae200.dambae200.global.common.DeleteResponse;
import io.swagger.models.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accesses")
@Slf4j
public class AccessRestController {

    final AccessService accessService;

    @GetMapping("")
    public ResponseEntity<AccessDto.GetListUserResponse> findAllByStoreId(@RequestParam @NotBlank String storeId){
        AccessDto.GetListUserResponse response = accessService.findAllByStoreId(Long.valueOf(storeId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by_user")
    public ResponseEntity<AccessDto.GetStoreListResponse> findAllByUserId(@RequestParam @NotBlank String userId){
        AccessDto.GetStoreListResponse response = accessService.findAllByUserId(Long.valueOf(userId));
        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity<AccessDto.GetResponse> applyAccess(@RequestBody @Valid AccessDto.ApplyRequest request){
        AccessDto.GetResponse response = accessService.applyAccess(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccessDto.GetResponse> updateAccessByStoreStaff(@PathVariable String id, @RequestParam String accessTypeCode){
        AccessDto.GetResponse response = accessService.updateAccess(Long.valueOf(id), accessTypeCode, false);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/byadmin")
    public ResponseEntity<AccessDto.GetResponse> updateAccessByStoreAdmin(@PathVariable String id, @RequestParam @NotNull String accessTypeCode){
        AccessDto.GetResponse response = accessService.updateAccess(Long.valueOf(id), accessTypeCode, true);
        return ResponseEntity.ok(response);
    }

    // TODO password 받는 법
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteAccess(@PathVariable String id){
        DeleteResponse response = accessService.deleteAccess(Long.valueOf(id));
        return ResponseEntity.ok(response);
    }

}
