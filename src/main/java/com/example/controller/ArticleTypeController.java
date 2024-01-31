package com.example.controller;

import com.example.dto.ArticleTypeCrudeDTO;
import com.example.dto.ArticleTypeDTO;
import com.example.dto.GetByLangDTO;
import com.example.enums.AppLanguage;
import com.example.service.ArticleTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articleType")
public class ArticleTypeController {
    private final ArticleTypeService articleTypeService;

    public ArticleTypeController(ArticleTypeService articleTypeService) {
        this.articleTypeService = articleTypeService;
    }

    @PostMapping("")
    public ResponseEntity<Boolean> create(@RequestBody ArticleTypeCrudeDTO dto) {
        return ResponseEntity.ok(articleTypeService.create(dto));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ArticleTypeDTO> updateById(@PathVariable(value = "id") Integer id,
                                                     @RequestBody ArticleTypeCrudeDTO dto) {
        return ResponseEntity.ok(articleTypeService.updateById(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Integer id) {
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
