package com.dambae200.dambae200.domain.access.controller;


import com.dambae200.dambae200.domain.access.dto.AccessApplyRequest;
import com.dambae200.dambae200.domain.access.dto.AccessGetResponse;
import com.dambae200.dambae200.domain.access.dto.AccessGetStoreListResponse;
import com.dambae200.dambae200.domain.access.dto.AccessGetUserListResponse;
import com.dambae200.dambae200.domain.access.service.AccessService;
import com.dambae200.dambae200.global.common.dto.DeleteResponse;
import com.dambae200.dambae200.global.common.dto.StandardResponse;
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

    private final AccessService accessService;

    @GetMapping("")
    public ResponseEntity<StandardResponse<AccessGetUserListResponse>> findAllByStoreId(@RequestParam @NotBlank String storeId){
        AccessGetUserListResponse response = accessService.findAllByStoreId(Long.valueOf(storeId));
        return StandardResponse.ofOk(response);
    }

    @GetMapping("/by_user")
    public ResponseEntity<StandardResponse<AccessGetStoreListResponse>> findAllByUserId(@RequestParam @NotBlank String userId){
        AccessGetStoreListResponse response = accessService.findAllByUserId(Long.valueOf(userId));
        return StandardResponse.ofOk(response);
    }

    @PostMapping("")
    public ResponseEntity<StandardResponse<AccessGetResponse>> applyAccess(@RequestBody @Valid AccessApplyRequest request){
        AccessGetResponse response = accessService.applyAccess(request);
        return StandardResponse.ofOk(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StandardResponse<AccessGetResponse>> updateAccessByStoreStaff(@PathVariable String id, @RequestParam String accessTypeCode){
        AccessGetResponse response = accessService.updateAccess(Long.valueOf(id), accessTypeCode, false);
        return StandardResponse.ofOk(response);
    }

    @PutMapping("/{id}/byadmin")
    public ResponseEntity<StandardResponse<AccessGetResponse>> updateAccessByStoreAdmin(@PathVariable String id, @RequestParam @NotNull String accessTypeCode){
        AccessGetResponse response = accessService.updateAccess(Long.valueOf(id), accessTypeCode, true);
        return StandardResponse.ofOk(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse<DeleteResponse>> deleteAccess(@PathVariable String id){
        DeleteResponse response = accessService.deleteAccess(Long.valueOf(id));
        return StandardResponse.ofOk(response);
    }

}
