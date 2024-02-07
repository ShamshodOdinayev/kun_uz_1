package com.example.controller;

import com.example.dto.ArticleCreateDTO;
import com.example.dto.ArticleDTO;
import com.example.entity.ArticleEntity;
import com.example.service.ArticleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping("/adm")
    public ResponseEntity<ArticleDTO> create(@RequestBody ArticleCreateDTO dto,
                                             HttpServletRequest request) {
        return ResponseEntity.ok(articleService.create(dto, 2));
    }

    @PutMapping("/adm/{id}")
    public ResponseEntity<ArticleDTO> update(@RequestBody ArticleCreateDTO dto,
                                             @PathVariable String id,
                                             HttpServletRequest request) {
        return ResponseEntity.ok(articleService.updateById(dto, id));
    }

    @DeleteMapping("/adm/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable String id) {
        return ResponseEntity.ok(articleService.deleteById(id));
    }

    @PutMapping("/adm/changeStatus/{id}")
    public ResponseEntity<Integer> changeStatusById(@PathVariable String id) {
        return ResponseEntity.ok(articleService.changeStatusById(id));
    }

    @GetMapping("/getTypeId")
    public ResponseEntity<?> getLastArticleByType(@RequestParam(value = "typeId", defaultValue = "1") Integer typeId,
                                                  @RequestParam(value = "size", defaultValue = "3") Integer size) {
        return ResponseEntity.ok(articleService.getLastArticleByType(typeId, size));
    }


}
