package com.example.controller;

import com.example.dto.CreateProfileDTO;
import com.example.dto.ProfileDTO;
import com.example.service.ProfileService;
import com.example.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("")
    public ResponseEntity<ProfileDTO> create(@RequestBody CreateProfileDTO dto,
                                             @RequestHeader(value = "Authorization") String jwt) {
        return JWTUtil.requestHeaderCheckAdmin(jwt) ? ResponseEntity.ok(profileService.create(dto)) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<ProfileDTO> update(@RequestBody CreateProfileDTO dto,
                                             @PathVariable Integer id,
                                             @RequestHeader(value = "Authorization") String jwt) {
        return JWTUtil.requestHeaderCheckAdmin(jwt) ? ResponseEntity.ok(profileService.update(dto, id)) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/detail")
    public ResponseEntity<ProfileDTO> update(@RequestBody CreateProfileDTO dto,
                                             @RequestHeader(value = "Authorization") String jwt) {
        return ResponseEntity.ok(profileService.update(dto, jwt));
    }

    @GetMapping("")
    public ResponseEntity<List<ProfileDTO>> getAll(@RequestHeader(value = "Authorization") String jwt) {
        return JWTUtil.requestHeaderCheckAdmin(jwt) ? ResponseEntity.ok(profileService.getAll()) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Integer id,
                                              @RequestHeader(value = "Authorization") String jwt) {
        return JWTUtil.requestHeaderCheckAdmin(jwt) ? ResponseEntity.ok(profileService.deleteById(id)) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
