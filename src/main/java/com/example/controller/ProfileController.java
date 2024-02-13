package com.example.controller;

import com.example.dto.CreateProfileDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.ProfileFilterDTO;
import com.example.service.ProfileService;
import com.example.util.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Profile API list")
@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ProfileDTO> create(@RequestBody CreateProfileDTO dto) {
        log.warn("Profile create {}", dto.getEmail());
        return ResponseEntity.ok(profileService.create(dto));
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ProfileDTO> update(@RequestBody CreateProfileDTO dto,
                                             @PathVariable Integer id) {
        log.warn("Profile admin update {}", id);
        return ResponseEntity.ok(profileService.update(dto, id));
    }

    @PutMapping("/detail")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ProfileDTO> update(@RequestBody CreateProfileDTO dto) {
        log.warn("Profile detail update{}", SpringSecurityUtil.getCurrentUser());
        return ResponseEntity.ok(profileService.update(dto));
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<ProfileDTO>> getAll() {
        log.warn("Profile get all{}", SpringSecurityUtil.getCurrentUser().getEmail());
        return ResponseEntity.ok(profileService.getAll());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Boolean> deleteById(@PathVariable Integer id) {
        log.warn("delete by id{}", SpringSecurityUtil.getCurrentUser().getEmail());
        return ResponseEntity.ok(profileService.deleteById(id));
    }

    @PostMapping("/adm/filter")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<PageImpl<ProfileDTO>> filter(@RequestBody ProfileFilterDTO dto,
                                                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                       @RequestParam(value = "size", defaultValue = "5") Integer size) {
        log.warn("filter{}", SpringSecurityUtil.getCurrentUser().getId());
        return ResponseEntity.ok(profileService.filter(dto, page - 1, size));
    }

}
