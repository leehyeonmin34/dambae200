package dambae200.dambae200.domain.cigaretteList.controller;

import dambae200.dambae200.domain.cigaretteList.domain.CigaretteList;
import dambae200.dambae200.domain.cigaretteList.dto.CigaretteListDto;
import dambae200.dambae200.domain.cigaretteList.service.CigaretteListFindService;
import dambae200.dambae200.domain.cigaretteList.service.CigaretteListUpdateService;
import dambae200.dambae200.domain.cigaretteOnList.dto.CigaretteOnListDto;
import dambae200.dambae200.domain.cigaretteOnList.service.CigaretteOnListFindService;
import dambae200.dambae200.domain.cigaretteOnList.service.CigaretteOnListUpdateService;
import dambae200.dambae200.global.common.DeleteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/cigaretteLists")
@RequiredArgsConstructor
public class CigaretteListRestController {

    final CigaretteListFindService cigaretteListFindService;
    final CigaretteListUpdateService cigaretteListUpdateService;
    final CigaretteOnListFindService cigaretteOnListFindService;


    @GetMapping("")
    public ResponseEntity<CigaretteListDto.GetResponse> findByStoreId(@RequestParam @NotBlank String storeId) {
        CigaretteListDto.GetResponse response = cigaretteListFindService.findByStoreId(Long.valueOf(storeId));
        return ResponseEntity.ok(response);
    }

    /*
    @GetMapping("/{id}/displayOrder")
    public ResponseEntity<CigaretteOnListDto.GetListCigaretteResponse> findInfoById(@PathVariable String id) {
        CigaretteOnListDto.GetListCigaretteResponse response = cigaretteOnListFindService.findAllByCigaretteListId(Long.valueOf(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/computerizedOrder")
    public ResponseEntity<CigaretteOnListDto.GetListCigaretteByComputerizedOrderResponse> findAllBySectionId(@PathVariable String listId,@PathVariable String sectionId){
        CigaretteOnListDto.GetListCigaretteByComputerizedOrderResponse response = cigaretteOnListFindService.findAllBySectionId(Long.valueOf(listId), Long.valueOf(sectionId));
        return ResponseEntity.ok(response);
    }

     */



    @PutMapping("/{id}")
    public ResponseEntity<CigaretteListDto.GetResponse> updateCigaretteListName(@PathVariable @NotNull Long id, @RequestBody @Valid CigaretteListDto.UpdateRequest request) {
        CigaretteListDto.GetResponse response = cigaretteListUpdateService.updateCigaretteList(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteCigaretteList(@PathVariable String id) {
        DeleteResponse response = cigaretteListUpdateService.deleteCigaretteList(Long.valueOf(id));
        return ResponseEntity.ok(response);
    }
}
