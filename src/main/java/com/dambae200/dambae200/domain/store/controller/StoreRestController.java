package com.dambae200.dambae200.domain.store.controller;

import com.dambae200.dambae200.domain.access.service.AccessService;
import com.dambae200.dambae200.domain.store.dto.StoreAddRequest;
import com.dambae200.dambae200.domain.store.dto.StoreGetListResponse;
import com.dambae200.dambae200.domain.store.dto.StoreGetResponse;
import com.dambae200.dambae200.domain.store.dto.StoreUpdateRequest;
import com.dambae200.dambae200.domain.store.service.StoreFindService;
import com.dambae200.dambae200.domain.store.service.StoreUpdateService;
import com.dambae200.dambae200.global.common.dto.DeleteResponse;
import com.dambae200.dambae200.global.common.dto.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URLDecoder;
import java.nio.charset.Charset;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreRestController {

    final StoreFindService storeFindService;
    final StoreUpdateService storeUpdateService;
    final AccessService accessService;


    @GetMapping("")
    public ResponseEntity<StandardResponse<StoreGetListResponse>> findAllByName(@RequestParam @NotNull String name){
        String decodedName = URLDecoder.decode(name, Charset.forName("UTF-8"));
        StoreGetListResponse response = storeFindService.findByNameLike(decodedName);
        return StandardResponse.ofOk(response);
    }

    @PostMapping("")
    public ResponseEntity<StandardResponse<StoreGetResponse>> addStore(@RequestBody @Valid StoreAddRequest request){
        StoreGetResponse response = storeUpdateService.addStore(request);
        return StandardResponse.ofOk(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StandardResponse<StoreGetResponse>> updateStore(@PathVariable @NotNull Long id, @RequestBody @Valid StoreUpdateRequest request){
        accessService.checkAdminAccess(request.getUserId(), id);
        StoreGetResponse response = storeUpdateService.updateStore(id, request);
        return StandardResponse.ofOk(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse<DeleteResponse>> deleteStore(@PathVariable @NotNull String id, @RequestParam @NotNull String userId){
        accessService.checkAdminAccess(Long.valueOf(userId), Long.valueOf(id));
        DeleteResponse response = storeUpdateService.deleteStore(Long.valueOf(id));
        return StandardResponse.ofOk(response);
    }





}
