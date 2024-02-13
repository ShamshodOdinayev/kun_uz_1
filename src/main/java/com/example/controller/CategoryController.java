package com.example.controller;

import com.example.dto.CategoryCreateDTO;
import com.example.dto.CategoryDTO;
import com.example.dto.GetByLangDTO;
import com.example.enums.AppLanguage;
import com.example.enums.ProfileRole;
import com.example.service.CategoryService;
import com.example.util.HttpRequestUtil;
import com.example.util.JWTUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Category API list")
@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/adm")
    public ResponseEntity<CategoryCreateDTO> create(@RequestBody CategoryCreateDTO dto,
                                                    HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ROLE_ADMIN);
        log.warn("Category create");
        return ResponseEntity.ok(categoryService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable Integer id,
                                        @RequestBody CategoryCreateDTO dto,
                                        @RequestHeader(value = "Authorization") String jwt) {
        log.warn("Category update {}", id);
        return JWTUtil.requestHeaderCheckAdmin(jwt) ? ResponseEntity.ok(categoryService.updateById(id, dto)) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Integer id,
                                              @RequestHeader(value = "Authorization") String jwt) {
        log.warn("Category delete {}", id);
        return JWTUtil.requestHeaderCheckAdmin(jwt) ? ResponseEntity.ok(categoryService.deleteById(id)) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("")
    public ResponseEntity<List<CategoryDTO>> getAll(@RequestHeader(value = "Authorization") String jwt) {
        log.warn("Category get all {}", jwt);
        return JWTUtil.requestHeaderCheckAdmin(jwt) ? ResponseEntity.ok(categoryService.getAll()) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/lang")
    public ResponseEntity<List<GetByLangDTO>> getByLang(@RequestParam(value = "lang", defaultValue = "uz") AppLanguage lang) {
        return ResponseEntity.ok(categoryService.getByLang(lang));
    }

}
