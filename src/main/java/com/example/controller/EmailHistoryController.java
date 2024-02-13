package com.example.controller;

import com.example.dto.EmailSendHistoryDTO;
import com.example.enums.ProfileRole;
import com.example.service.EmailSendHistoryService;
import com.example.util.HttpRequestUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Tag(name = "Email history API list")
@RestController
@RequestMapping("/emailHistory")
public class EmailHistoryController {
    @Autowired
    private EmailSendHistoryService emailSendHistoryServices;

    @GetMapping("/adm/email/{email}")
    public ResponseEntity<List<EmailSendHistoryDTO>> get(@PathVariable String email,
                                                         HttpServletRequest request) {
        Integer profileId = HttpRequestUtil.getProfileId(request, ProfileRole.ROLE_ADMIN);
        log.warn("Email history get {} {}", profileId, email);
        return ResponseEntity.ok(emailSendHistoryServices.get(email));
    }

    @GetMapping("/adm/date/{date}")
    public ResponseEntity<List<EmailSendHistoryDTO>> getByGiven(@PathVariable LocalDate date,
                                                                HttpServletRequest request) {
        Integer profileId = HttpRequestUtil.getProfileId(request, ProfileRole.ROLE_ADMIN);
        log.warn("get By Given {} {}", date, profileId);
        return ResponseEntity.ok(emailSendHistoryServices.getByGiven(date));
    }


}
