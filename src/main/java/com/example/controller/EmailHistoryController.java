package com.example.controller;

import com.example.dto.EmailSendHistoryDTO;
import com.example.enums.ProfileRole;
import com.example.service.EmailSendHistoryService;
import com.example.util.HttpRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/emailHistory")
public class EmailHistoryController {
    @Autowired
    private EmailSendHistoryService emailSendHistoryServices;

    @GetMapping("/adm/email/{email}")
    public ResponseEntity<List<EmailSendHistoryDTO>> get(@PathVariable String email,
                                                         HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(emailSendHistoryServices.get(email));
    }

    @GetMapping("/adm/date/{date}")
    public ResponseEntity<List<EmailSendHistoryDTO>> getByGiven(@PathVariable LocalDate date,
                                                                HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(emailSendHistoryServices.getByGiven(date));
    }


}
