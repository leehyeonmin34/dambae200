package com.dambae200.dambae200.domain.cigarette.controller;

import com.dambae200.dambae200.domain.cigarette.dto.CigaretteDto;
import com.dambae200.dambae200.domain.cigarette.service.CigaretteFindService;
import com.dambae200.dambae200.domain.cigarette.service.CigaretteUpdateService;
import com.dambae200.dambae200.global.common.DeleteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.List;

@RestController
@RequestMapping("/api/cigarettes")
@RequiredArgsConstructor
public class CigaretteRestController {

    final CigaretteFindService cigaretteFindService;
    final CigaretteUpdateService cigaretteUpdateService;


    @GetMapping("/all")
    public ResponseEntity<CigaretteDto.GetListResponse> findAllCigarette() {
        CigaretteDto.GetListResponse response = cigaretteFindService.findAllCigarettes();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/drop_box")
    public ResponseEntity<CigaretteDto.GetListResponse> findAllByOfficialNameLike(@RequestParam @NotNull String name) {
        String decodedName = URLDecoder.decode(name, Charset.forName("UTF-8"));
        CigaretteDto.GetListResponse response = cigaretteFindService.findAllByOfficialNameLike(decodedName);
        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity<CigaretteDto.GetResponse> addCigarette(@RequestBody @Valid CigaretteDto.CigaretteRequest request) {
        CigaretteDto.GetResponse response = cigaretteUpdateService.addCigarette(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/multiple")
    public ResponseEntity<CigaretteDto.GetListResponse> addCigarettes(@RequestBody @Valid List<CigaretteDto.CigaretteRequest> request) {
        CigaretteDto.GetListResponse response = cigaretteUpdateService.addAllCigarette(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CigaretteDto.GetResponse> updateCigarette(@PathVariable @NotNull Long id, @RequestBody @Valid CigaretteDto.CigaretteRequest request) {
        CigaretteDto.GetResponse response = cigaretteUpdateService.updateCigarette(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteCigarette(@PathVariable String id) {
        DeleteResponse response = cigaretteUpdateService.deleteCigarette(Long.valueOf(id));
        return ResponseEntity.ok(response);
    }


}
