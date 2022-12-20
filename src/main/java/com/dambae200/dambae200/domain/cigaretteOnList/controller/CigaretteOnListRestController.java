package com.dambae200.dambae200.domain.cigaretteOnList.controller;

import com.dambae200.dambae200.domain.access.service.AccessService;
import com.dambae200.dambae200.domain.cigaretteOnList.dto.CigaretteOnListGetListResponse;
import com.dambae200.dambae200.domain.cigaretteOnList.service.CigaretteOnListFindService;
import com.dambae200.dambae200.domain.cigaretteOnList.service.CigaretteOnListUpdateService;
import com.dambae200.dambae200.global.common.dto.StandardResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cigarette_on_lists")
@RequiredArgsConstructor
public class CigaretteOnListRestController {

    final CigaretteOnListFindService cigaretteOnListFindService;
    final CigaretteOnListUpdateService cigaretteOnListUpdateService;
    final AccessService accessService;

    //리스트에 있는 담배 보여주기(진열순서)
    @GetMapping("/display_order")
    public ResponseEntity<StandardResponse<CigaretteOnListGetListResponse>> findAllByStoreIdOrderByDisplay(@RequestParam @NonNull String storeId, @RequestParam @NonNull String requestUserId) {
        accessService.checkAccess(Long.valueOf(requestUserId), Long.valueOf(storeId));
        CigaretteOnListGetListResponse response = cigaretteOnListFindService.findAllByStoreIdOrderByDisplay(Long.valueOf(storeId), Long.valueOf(requestUserId));
        return StandardResponse.ofOk(response);
    }

}
