package com.example.controller;

import com.example.dto.RegionDTO;
import com.example.enums.AppLanguage;
import com.example.service.RegionService;
import com.example.util.JWTUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/region")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @PostMapping("")
    public ResponseEntity<Boolean> create(@Valid @RequestBody RegionDTO dto,
                                          @RequestHeader(value = "Authorization") String jwt) {
        return JWTUtil.requestHeaderCheckAdmin(jwt) ? ResponseEntity.ok(regionService.create(dto)) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<RegionDTO> updateById(@Valid @PathVariable Integer id,
                                                @RequestBody RegionDTO dto,
                                                @RequestHeader(value = "Authorization") String jwt) {
        return JWTUtil.requestHeaderCheckAdmin(jwt) ? ResponseEntity.ok(regionService.updateById(dto, id)) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Integer id,
                                              @RequestHeader(value = "Authorization") String jwt) {
        return JWTUtil.requestHeaderCheckAdmin(jwt) ? ResponseEntity.ok(regionService.deleteById(id)) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("")
    public ResponseEntity<List<RegionDTO>> getAll(@RequestHeader(value = "Authorization") String jwt) {
        return JWTUtil.requestHeaderCheckAdmin(jwt) ? ResponseEntity.ok(regionService.getAll()) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/lang")
    public ResponseEntity<?> getByLang(@RequestParam(value = "lang", defaultValue = "uz") AppLanguage lang,
                                       @RequestHeader(value = "Authorization") String jwt) {
        return JWTUtil.requestHeaderCheckAdmin(jwt) ? ResponseEntity.ok(regionService.getByLang(lang)) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
