package dambae200.dambae200.domain.section.controller;

import dambae200.dambae200.domain.section.dto.SectionDto;
import dambae200.dambae200.domain.section.service.SectionFIndService;
import dambae200.dambae200.domain.section.service.SectionUpdateService;
import dambae200.dambae200.global.common.DeleteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/sections")
@RequiredArgsConstructor
public class SectionRestController {

    final SectionFIndService sectionFIndService;
    final SectionUpdateService sectionUpdateService;

    @GetMapping("/v1")
    public ResponseEntity<SectionDto.GetListResponse> findAllOrderByOrder() {
        SectionDto.GetListResponse response = sectionFIndService.findAllOrderByOrder();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v2")
    public ResponseEntity<SectionDto.GetListResponse> findAll(){
        SectionDto.GetListResponse response = sectionFIndService.findAll();
        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity<SectionDto.GetResponse> addSection(@RequestBody @Valid SectionDto.SectionRequest request) {
        SectionDto.GetResponse response = sectionUpdateService.addSection(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SectionDto.GetResponse> updateSection(@PathVariable @NotNull Long id, @RequestBody @Valid SectionDto.SectionRequest request) {
        SectionDto.GetResponse response = sectionUpdateService.updateSection(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteSection(@PathVariable String id) {
        DeleteResponse response = sectionUpdateService.deleteSection(Long.valueOf(id));
        return ResponseEntity.ok(response);
    }
}
