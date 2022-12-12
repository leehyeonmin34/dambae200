package com.dambae200.dambae200.domain.cigaretteOnList.controller;

import com.dambae200.dambae200.domain.access.service.AccessService;
import com.dambae200.dambae200.domain.cigaretteOnList.dto.CigaretteOnListAddRequest;
import com.dambae200.dambae200.domain.cigaretteOnList.dto.CigaretteOnListGetListResponse;
import com.dambae200.dambae200.domain.cigaretteOnList.dto.CigaretteOnListGetResponse;
import com.dambae200.dambae200.domain.cigaretteOnList.dto.CigaretteOnListUpdateCountRequest;
import com.dambae200.dambae200.domain.cigaretteOnList.service.CigaretteOnListFindService;
import com.dambae200.dambae200.domain.cigaretteOnList.service.CigaretteOnListUpdateService;
import com.dambae200.dambae200.global.common.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cigarette_on_lists")
@RequiredArgsConstructor
public class CigaretteOnListRestController {

    final CigaretteOnListFindService cigaretteOnListFindService;
    final CigaretteOnListUpdateService cigaretteOnListUpdateService;
    final AccessService accessService;

    //리스트에 있는 담배 보여주기(진열순서)
    @GetMapping("/{id}/display_order")
    public ResponseEntity<StandardResponse<CigaretteOnListGetListResponse>> findAllByStoreIdOrderByDisplay(@PathVariable String id, @RequestParam String requestUserId) {
        accessService.checkAccess(Long.valueOf(requestUserId), Long.valueOf(id));
        CigaretteOnListGetListResponse response = cigaretteOnListFindService.findAllByStoreIdOrderByDisplay(Long.valueOf(id), Long.valueOf(requestUserId));
        return StandardResponse.ofOk(response);
    }

    //리스트에 있는 담배 보여주기(전산순서)
    @GetMapping("/{id}/computerized_order")
    public ResponseEntity<StandardResponse<CigaretteOnListGetListResponse>> findAllByStoreIdOrderByComputerized(@PathVariable String id, @RequestParam String requestUserId) {
        accessService.checkAccess(Long.valueOf(requestUserId), Long.valueOf(id));
        CigaretteOnListGetListResponse response = cigaretteOnListFindService.findAllByStoreIdOrderByComputerized(Long.valueOf(id), Long.valueOf(requestUserId));
        return StandardResponse.ofOk(response);
    }

//    @PutMapping("/{id}/draganddrop_display")
//    public ResponseEntity<CigaretteOnListGetListResponse> dragAndDropDisplayOrder(@PathVariable String id, @RequestBody @Valid CigaretteOnListDto.DragAndDropRequest request){
//        CigaretteOnListGetListResponse response = cigaretteOnListUpdateService.moveDisplayOrderByDragAndDrop(Long.valueOf(id), request.getAfterElementId(), request.getDraggableId());
//        return ResponseEntity.ok(response);
//    }
//
//    @PutMapping("/{id}/draganddrop_computerized")
//    public ResponseEntity<CigaretteOnListGetListResponse> dragAndDropComputerizedOrder(@PathVariable String id, @RequestBody @Valid CigaretteOnListDto.DragAndDropRequest request){
//        CigaretteOnListGetListResponse response = cigaretteOnListUpdateService.moveComputerizedOrderByDragAndDrop(Long.valueOf(id), request.getAfterElementId(), request.getDraggableId());
//        return ResponseEntity.ok(response);
//    }

//    @PutMapping("/reorder_display")
//    public ResponseEntity<CigaretteOnListGetListResponse> reOrderDisplay(@RequestBody @Valid List<CigaretteOnListDto.ReorderRequest> request){
//        CigaretteOnListGetListResponse response = cigaretteOnListUpdateService.reOrderDisplay(request);
//        return ResponseEntity.ok(response);
//    }

//    @PutMapping("/reorder_computerized")
//    public ResponseEntity<CigaretteOnListGetListResponse> reOrderComputerized(@RequestBody @Valid List<CigaretteOnListDto.ReorderRequest> request){
//        CigaretteOnListGetListResponse response = cigaretteOnListUpdateService.reOrderComputerized(request);
//        return ResponseEntity.ok(response);
//    }
}
