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
        final CigaretteGetListResponse response = cigaretteFindService.findAllCigarettes();
        return StandardResponse.ofOk(response);
    }

    @PostMapping("")
    public ResponseEntity<StandardResponse<CigaretteGetResponse>> addCigarette(@RequestBody @Valid final CigaretteAddRequest request) {
        final CigaretteGetResponse response = cigaretteUpdateService.addCigarette(request);
        return StandardResponse.ofOk(response);
    }

    @PostMapping("/multiple")
    public ResponseEntity<StandardResponse<CigaretteGetListResponse>> addCigarettes(@RequestBody @Valid final List<CigaretteAddRequest> request) {
        final CigaretteGetListResponse response = cigaretteUpdateService.addAllCigarette(request);
        return StandardResponse.ofOk(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StandardResponse<CigaretteGetResponse>> updateCigarette(@PathVariable @NotNull final Long id, @RequestBody @Valid final CigaretteUpdateRequest request) {
        final CigaretteGetResponse response = cigaretteUpdateService.updateCigarette(id, request);
        return StandardResponse.ofOk(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse<DeleteResponse>> deleteCigarette(@PathVariable final String id) {
        final DeleteResponse response = cigaretteUpdateService.deleteCigarette(Long.valueOf(id));
        return StandardResponse.ofOk(response);
    }


}
