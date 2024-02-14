package com.example.controller;

import com.example.dto.CommentCreateDTO;
import com.example.dto.CommentDTO;
import com.example.dto.CommentUpdateDTO;
import com.example.service.CommentService;
import com.example.util.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Comment API list")
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("")
    public ResponseEntity<CommentDTO> create(@RequestBody CommentCreateDTO dto) {
        Integer id = SpringSecurityUtil.getCurrentUser().getId();
        System.out.println(id);
        log.info("Comment create{}", id);
        return ResponseEntity.ok(commentService.create(dto, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateById(@RequestBody CommentUpdateDTO dto,
                                                 @PathVariable Integer id) {
        return ResponseEntity.ok(commentService.update(dto, id));
    }

}
