package com.example.controller;

import com.example.dto.CommentCreateDTO;
import com.example.dto.CommentDTO;
import com.example.dto.CommentFilterDTO;
import com.example.dto.CommentUpdateDTO;
import com.example.service.CommentService;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(commentService.delete(id));
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<List<CommentDTO>> getByArticleId(@PathVariable String articleId) {
        return ResponseEntity.ok(commentService.getByArticleId(articleId));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/pagination")
    public ResponseEntity<PageImpl<CommentDTO>> pagination(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                           @RequestParam(value = "size", defaultValue = "5") Integer size) {
        return ResponseEntity.ok(commentService.pagination(page - 1, size));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/filter")
    public ResponseEntity<PageImpl<CommentDTO>> filter(@RequestBody CommentFilterDTO dto,
                                                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                       @RequestParam(value = "size", defaultValue = "5") Integer size) {
        return ResponseEntity.ok(commentService.filter(dto, page - 1, size));
    }


}
