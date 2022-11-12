package com.dambae200.dambae200.domain.cigaretteOnList.controller;

import com.dambae200.dambae200.domain.cigaretteOnList.dto.CigaretteOnListDto;
import com.dambae200.dambae200.domain.cigaretteOnList.service.CigaretteOnListFindService;
import com.dambae200.dambae200.domain.cigaretteOnList.service.CigaretteOnListUpdateService;
import com.dambae200.dambae200.global.common.DeleteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cigaretteOnLists")
@RequiredArgsConstructor
public class CigaretteOnListRestController {

    final CigaretteOnListFindService cigaretteOnListFindService;
    final CigaretteOnListUpdateService cigaretteOnListUpdateService;

    //리스트에 있는 담배 보여주기(진열순서)
    @GetMapping("/{id}/display_order")
    public ResponseEntity<CigaretteOnListDto.GetListCigaretteResponse> findAllByCigaretteListId(@PathVariable String id) {
        CigaretteOnListDto.GetListCigaretteResponse response = cigaretteOnListFindService.findAllByCigaretteListIdOrderByDisplay(Long.valueOf(id));
        return ResponseEntity.ok(response);
    }

    //리스트에 있는 담배 보여주기(전산순서)
    @GetMapping("/{id}/computerized_order")
    public ResponseEntity<CigaretteOnListDto.GetListCigaretteResponse> findAllByCigaretteListIdOrderByComputerized(@PathVariable String id) {
        CigaretteOnListDto.GetListCigaretteResponse response = cigaretteOnListFindService.findAllByCigaretteListIdOrderByComputerized(Long.valueOf(id));
        return ResponseEntity.ok(response);
    }

    /*
    //진열순서 검색 기능.
    @GetMapping("/{id}/include")
    public ResponseEntity<CigaretteOnListDto.GetListCigaretteResponse> findAllByOfficialName(@PathVariable String id, @RequestParam @NotNull String name){
        String decodedName = URLDecoder.decode(name, Charset.forName("UTF-8"));
        CigaretteOnListDto.GetListCigaretteResponse response = cigaretteOnListFindService.findAllByOfficialName(Long.valueOf(id), decodedName);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/not_include")
    public ResponseEntity<CigaretteOnListDto.GetListCigaretteResponse> findAllByNotIncludeOfficialName(@PathVariable String id, @RequestParam @NotNull String name){
        String decodedName = URLDecoder.decode(name, Charset.forName("UTF-8"));
        CigaretteOnListDto.GetListCigaretteResponse response = cigaretteOnListFindService.findAllByNotIncludeOfficialName(Long.valueOf(id), decodedName);
        return ResponseEntity.ok(response);

     */


    @PostMapping("")
    public ResponseEntity<CigaretteOnListDto.GetCigaretteResponse> addCigaretteOnListById(@RequestBody @Valid CigaretteOnListDto.AddCigaretteOnList request) {
        CigaretteOnListDto.GetCigaretteResponse response = cigaretteOnListUpdateService.addCigaretteOnListById(request);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}/update_count")
    public ResponseEntity<CigaretteOnListDto.GetCigaretteResponse> inputCigaretteCount(@PathVariable String id, @RequestBody @Valid CigaretteOnListDto.UpdateCountRequest request) {
        CigaretteOnListDto.GetCigaretteResponse response = cigaretteOnListUpdateService.inputCigaretteCount(Long.valueOf(id), request.getCount());
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}/draganddrop_display")
    public ResponseEntity<CigaretteOnListDto.GetListCigaretteResponse> dragAndDropDisplayOrder(@PathVariable String id, @RequestBody @Valid CigaretteOnListDto.DragAndDropRequest request){
        CigaretteOnListDto.GetListCigaretteResponse response = cigaretteOnListUpdateService.moveDisplayOrderByDragAndDrop(Long.valueOf(id), request.getAfterElementId(), request.getDraggableId());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/draganddrop_computerized")
    public ResponseEntity<CigaretteOnListDto.GetListCigaretteResponse> dragAndDropComputerizedOrder(@PathVariable String id, @RequestBody @Valid CigaretteOnListDto.DragAndDropRequest request){
        CigaretteOnListDto.GetListCigaretteResponse response = cigaretteOnListUpdateService.moveComputerizedOrderByDragAndDrop(Long.valueOf(id), request.getAfterElementId(), request.getDraggableId());
        return ResponseEntity.ok(response);
    }

    /*
    @PutMapping("{id}/move_cigarette_by_select")
    public void moveCigaretteBySelect(){
        final List<String> nullRemovedIds = ids.stream().filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> resultIds = null;
        for (String id : nullRemovedIds){
            resultIds.add(Long.valueOf(id));
        }

        CigaretteOnListDto.GetListCigaretteResponse response = cigaretteOnListUpdateService.moveCigaretteBySelect(Long.valueOf(listId), resultIds, Long.valueOf(selectedId));
        //return ResponseEntity.ok(response);
    }
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteCigaretteOnList(@PathVariable String id) {
        DeleteResponse response = cigaretteOnListUpdateService.deleteCigaretteOnList(Long.valueOf(id));
        return ResponseEntity.ok(response);
    }
}
