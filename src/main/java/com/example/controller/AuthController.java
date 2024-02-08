package com.example.controller;

import com.example.dto.AuthDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.RegistrationDTO;
import com.example.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Authorization Api list", description = "Api list for Authorization")
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Api for login", description = "this api used for authorization")
    public ResponseEntity<ProfileDTO> login(@RequestBody AuthDTO dto) {
        log.trace("Login In Trace");
        log.debug("Login In Debug");

        log.info("Login {} ", dto.getEmail());
        log.warn("Login {} ", dto.getEmail());
        log.error("Login {} ", dto.getEmail());

        return ResponseEntity.ok(authService.auth(dto));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody RegistrationDTO dto) {
        log.info("registration {} ", dto.getEmail());
        return ResponseEntity.ok(authService.registration(dto));
    }

    @GetMapping("/verification/email/{jwt}")
    public ResponseEntity<String> emailVerification(@PathVariable("jwt") String jwt) {
        log.info("verification {} ", jwt);
        return ResponseEntity.ok(authService.emailVerification(jwt));
    }

}
