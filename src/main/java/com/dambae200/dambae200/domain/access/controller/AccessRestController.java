package com.dambae200.dambae200.domain.access.controller;


import com.dambae200.dambae200.domain.access.dto.AccessDto;
import com.dambae200.dambae200.domain.access.service.AccessService;
import com.dambae200.dambae200.domain.user.dto.UserDto;
import com.dambae200.dambae200.global.common.DeleteResponse;
import io.swagger.models.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accesses")
public class AccessRestController {

    final AccessService accessService;

    @GetMapping("")
    public ResponseEntity<AccessDto.GetListUserResponse> findAllByStoreId(@RequestParam @NotBlank String storeId){
        AccessDto.GetListUserResponse response = accessService.findAllByStoreId(Long.valueOf(storeId));
        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity<AccessDto.GetResponse> applyAccess(@RequestBody @Valid AccessDto.ApplyRequest request){
        System.out.println(request.toString());
        AccessDto.GetResponse response = accessService.addAccess(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccessDto.GetResponse> updateAccessByStoreStaff(@PathVariable String id, @RequestParam String accessTypeCode){
        AccessDto.GetResponse response = accessService.updateAccess(Long.valueOf(id), accessTypeCode, false);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/byadmin")
    public ResponseEntity<AccessDto.GetResponse> updateAccessByStoreAdmin(@PathVariable String id, @RequestParam String accessTypeCode){
        AccessDto.GetResponse response = accessService.updateAccess(Long.valueOf(id), accessTypeCode, true);
        return ResponseEntity.ok(response);
    }

    // TODO password 받는 법
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteUser(@PathVariable String id){
        DeleteResponse response = accessService.deleteAccess(Long.valueOf(id));
        return ResponseEntity.ok(response);
    }

}
