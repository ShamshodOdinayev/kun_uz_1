package com.example.controller;

import com.example.dto.RegionDTO;
import com.example.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/region")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @PostMapping("")
    public ResponseEntity<Boolean> create(@RequestBody RegionDTO dto) {
        return ResponseEntity.ok(regionService.create(dto));
    }

    @PostMapping("/{id}")
    public ResponseEntity<RegionDTO> updateById(@PathVariable Integer id,
                                                @RequestBody RegionDTO dto) {
        return ResponseEntity.ok(regionService.updateById(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Integer id) {
        return ResponseEntity.ok(regionService.deleteById(id));
    }

    @GetMapping("")
    public ResponseEntity<List<RegionDTO>> getAll() {
        return ResponseEntity.ok(regionService.getAll());
    }

    @GetMapping("/lang")
    public ResponseEntity<?> getByLang(@RequestParam(value = "lang", defaultValue = "uz") String lang) {
        return ResponseEntity.ok(regionService.getByLang(lang));
    }

}
