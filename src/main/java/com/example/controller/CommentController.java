package com.example.controller;

import com.example.dto.CommentCreateDTO;
import com.example.dto.CommentDTO;
import com.example.service.CommentService;
import com.example.util.HttpRequestUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Comment API list")
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("")
    public ResponseEntity<CommentDTO> create(@RequestBody CommentCreateDTO dto,
                                             HttpServletRequest request) {
        Integer profileId = HttpRequestUtil.getProfileId(request);
        log.info("Comment create{}", profileId);
        return ResponseEntity.ok(commentService.create(dto, profileId));
    }

}
