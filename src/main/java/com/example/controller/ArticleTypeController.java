package com.example.controller;

import com.example.dto.ArticleTypeCrudeDTO;
import com.example.dto.ArticleTypeDTO;
import com.example.dto.GetByLangDTO;
import com.example.enums.AppLanguage;
import com.example.service.ArticleTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Article type API list")
@RestController
@RequestMapping("/articleType")
public class ArticleTypeController {
    private final ArticleTypeService articleTypeService;

    public ArticleTypeController(ArticleTypeService articleTypeService) {
        this.articleTypeService = articleTypeService;
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    public ResponseEntity<Boolean> create(@RequestBody ArticleTypeCrudeDTO dto) {
        log.warn("Article type create {}", dto.getOrderNumber());
        return ResponseEntity.ok(articleTypeService.create(dto));
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR','PUBLISHER')")
    public ResponseEntity<ArticleTypeDTO> updateById(@PathVariable(value = "id") Integer id,
                                                     @RequestBody ArticleTypeCrudeDTO dto) {
        log.warn("Article type update {}", id);
        return ResponseEntity.ok(articleTypeService.updateById(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR','PUBLISHER')")
    public ResponseEntity<Boolean> deleteById(@PathVariable Integer id) {
        log.warn("Article delete {}", id);
        return ResponseEntity.ok(articleTypeService.deleteById(id));
    }

    @GetMapping("")
    public ResponseEntity<List<ArticleTypeDTO>> getAll() {
        return ResponseEntity.ok(articleTypeService.getAll());
    }

    @GetMapping("/lang")
    public ResponseEntity<List<GetByLangDTO>> getByLang(@RequestParam(value = "lang", defaultValue = "uz") AppLanguage lang) {
        return ResponseEntity.ok(articleTypeService.getByLang(lang));
    }
}
