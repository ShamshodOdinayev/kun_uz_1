package com.example.controller;

import com.example.dto.AttachDTO;
import com.example.service.AttachService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Tag(name = "Attach API list")
@RestController
@RequestMapping("/attach")
public class AttachController {
    @Autowired
    private AttachService attachService;

    @GetMapping(value = "/open/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] open(@PathVariable("fileName") String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            try {
                return this.attachService.loadImage(fileName);
            } catch (Exception e) {
                log.error(e.getMessage());
                return new byte[0];
            }
        }
        return null;
    }

    @GetMapping(value = "/open_general/{fileName}", produces = MediaType.ALL_VALUE)
    public byte[] open_general(@PathVariable("fileName") String fileName) {
        return attachService.open_general(fileName);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR','PUBLISHER')")
    public ResponseEntity<AttachDTO> upload(@RequestParam("file") MultipartFile file) {
        log.warn("Attach upload");
        AttachDTO dto = attachService.save(file);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/download/{fineName}")
    public ResponseEntity<Resource> download(@PathVariable("fineName") String fileName) {
        log.warn("Attach download");
        return attachService.downloadFile(fileName);
    }


}
