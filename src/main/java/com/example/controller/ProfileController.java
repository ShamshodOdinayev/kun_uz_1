package com.example.controller;

import com.example.dto.CreateProfileDTO;
import com.example.dto.JwtDTO;
import com.example.dto.ProfileDTO;
import com.example.enums.ProfileRole;
import com.example.service.ProfileService;
import com.example.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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
    public ResponseEntity<ProfileDTO> updateForAdmin(@RequestBody CreateProfileDTO dto,
                                                     @PathVariable Integer id,
                                                     @RequestHeader(value = "Authorization") String jwt) {
        JwtDTO jwtDTO = JWTUtil.decode(jwt);
        if (jwtDTO.getRole().equals(ProfileRole.ADMIN)) {
            return JWTUtil.requestHeaderCheckAdmin(jwt) ? ResponseEntity.ok(profileService.update(dto, id)) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else if (jwtDTO.getRole().equals(ProfileRole.USER) && Objects.equals(jwtDTO.getId(), id)) {
            return ResponseEntity.ok(profileService.update(dto, id));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
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
