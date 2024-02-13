package com.example.controller;

import com.example.dto.RegionDTO;
import com.example.enums.AppLanguage;
import com.example.service.RegionService;
import com.example.util.JWTUtil;
import com.example.util.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Region API list")
@RestController
@RequestMapping("/region")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @PostMapping("")
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR','PUBLISHER')")
    public ResponseEntity<Boolean> create(@Valid @RequestBody RegionDTO dto) {
//        log.warn("create region{}", jwt);
        return ResponseEntity.ok(regionService.create(dto));
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR','PUBLISHER')")
    public ResponseEntity<RegionDTO> updateById(@Valid @PathVariable Integer id,
                                                @RequestBody RegionDTO dto) {
        log.warn("region update by id{}{}", id, SpringSecurityUtil.getCurrentUser().getId());
        return ResponseEntity.ok(regionService.updateById(dto, id));
//        return JWTUtil.requestHeaderCheckAdmin(jwt) ? ResponseEntity.ok(regionService.updateById(dto, id)) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR','PUBLISHER')")
    public ResponseEntity<Boolean> deleteById(@PathVariable Integer id) {
        log.warn("delete region{}", id);
        return ResponseEntity.ok(regionService.deleteById(id));
//        return JWTUtil.requestHeaderCheckAdmin(jwt) ? ResponseEntity.ok(regionService.deleteById(id)) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("")
    public ResponseEntity<List<RegionDTO>> getAll() {
        return ResponseEntity.ok(regionService.getAll());
//        return JWTUtil.requestHeaderCheckAdmin(jwt) ? ResponseEntity.ok(regionService.getAll()) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/lang")
    public ResponseEntity<?> getByLang(@RequestParam(value = "lang", defaultValue = "uz") AppLanguage lang) {
        return ResponseEntity.ok(regionService.getByLang(lang));
//        return JWTUtil.requestHeaderCheckAdmin(jwt) ? ResponseEntity.ok(regionService.getByLang(lang)) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
