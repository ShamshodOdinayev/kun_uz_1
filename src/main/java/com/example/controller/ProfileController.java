package com.example.controller;

import com.example.dto.CreateProfileDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.ProfileFilterDTO;
import com.example.enums.ProfileRole;
import com.example.service.ProfileService;
import com.example.util.HttpRequestUtil;
import com.example.util.JWTUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ProfileDTO> create(@RequestBody CreateProfileDTO dto,
                                             @RequestHeader(value = "Authorization") String jwt) {
        log.warn("Profile create {}", jwt);
        return JWTUtil.requestHeaderCheckAdmin(jwt) ? ResponseEntity.ok(profileService.create(dto)) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<ProfileDTO> update(@RequestBody CreateProfileDTO dto,
                                             @PathVariable Integer id,
                                             @RequestHeader(value = "Authorization") String jwt) {
        log.warn("Profile admin update {} {}", id, jwt);
        return JWTUtil.requestHeaderCheckAdmin(jwt) ? ResponseEntity.ok(profileService.update(dto, id)) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/detail")
    public ResponseEntity<ProfileDTO> update(@RequestBody CreateProfileDTO dto,
                                             @RequestHeader(value = "Authorization") String jwt) {
        log.warn("Profile detail update{}", jwt);
        return ResponseEntity.ok(profileService.update(dto, jwt));
    }

    @GetMapping("")
    public ResponseEntity<List<ProfileDTO>> getAll(@RequestHeader(value = "Authorization") String jwt) {
        log.warn("Profile get all{}", jwt);
        return JWTUtil.requestHeaderCheckAdmin(jwt) ? ResponseEntity.ok(profileService.getAll()) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Integer id,
                                              @RequestHeader(value = "Authorization") String jwt) {
        log.warn("delete by id{}", jwt);
        return JWTUtil.requestHeaderCheckAdmin(jwt) ? ResponseEntity.ok(profileService.deleteById(id)) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping("/adm/filter")
    public ResponseEntity<PageImpl<ProfileDTO>> filter(@RequestBody ProfileFilterDTO dto,
                                                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                       @RequestParam(value = "size", defaultValue = "5") Integer size,
                                                       HttpServletRequest request) {
        Integer profileId = HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        log.warn("filter{}", profileId);
        return ResponseEntity.ok(profileService.filter(dto, page - 1, size));
    }

}
