package com.dambae200.dambae200.domain.cigarette.controller;

import com.dambae200.dambae200.domain.cigarette.dto.CigaretteAddRequest;
import com.dambae200.dambae200.domain.cigarette.dto.CigaretteGetListResponse;
import com.dambae200.dambae200.domain.cigarette.dto.CigaretteGetResponse;
import com.dambae200.dambae200.domain.cigarette.dto.CigaretteUpdateRequest;
import com.dambae200.dambae200.domain.cigarette.service.CigaretteFindService;
import com.dambae200.dambae200.domain.cigarette.service.CigaretteUpdateService;
import com.dambae200.dambae200.global.common.dto.DeleteResponse;
import com.dambae200.dambae200.global.common.dto.StandardResponse;
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
    public ResponseEntity<StandardResponse<CigaretteGetListResponse>> findAllCigarette() {
        CigaretteGetListResponse response = cigaretteFindService.findAllCigarettes();
        return StandardResponse.ofOk(response);
    }

    @GetMapping("/drop_box")
    public ResponseEntity<StandardResponse<CigaretteGetListResponse>> findAllByOfficialNameLike(@RequestParam @NotNull String name) {
        String decodedName = URLDecoder.decode(name, Charset.forName("UTF-8"));
        CigaretteGetListResponse response = cigaretteFindService.findAllByOfficialNameLike(decodedName);
        return StandardResponse.ofOk(response);
    }

    @PostMapping("")
    public ResponseEntity<StandardResponse<CigaretteGetResponse>> addCigarette(@RequestBody @Valid CigaretteAddRequest request) {
        CigaretteGetResponse response = cigaretteUpdateService.addCigarette(request);
        return StandardResponse.ofOk(response);
    }

    @PostMapping("/multiple")
    public ResponseEntity<StandardResponse<CigaretteGetListResponse>> addCigarettes(@RequestBody @Valid List<CigaretteAddRequest> request) {
        CigaretteGetListResponse response = cigaretteUpdateService.addAllCigarette(request);
        return StandardResponse.ofOk(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StandardResponse<CigaretteGetResponse>> updateCigarette(@PathVariable @NotNull Long id, @RequestBody @Valid CigaretteUpdateRequest request) {
        CigaretteGetResponse response = cigaretteUpdateService.updateCigarette(id, request);
        return StandardResponse.ofOk(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse<DeleteResponse>> deleteCigarette(@PathVariable String id) {
        DeleteResponse response = cigaretteUpdateService.deleteCigarette(Long.valueOf(id));
        return StandardResponse.ofOk(response);
    }


}
