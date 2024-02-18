package com.example.controller;

import com.example.dto.*;
import com.example.service.ArticleService;
import com.example.util.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Article Api list", description = "Api list for Article")
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping("/adm")
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    public ResponseEntity<ArticleDTO> create(@RequestBody ArticleCreateDTO dto) {
        log.warn("Article create");
        return ResponseEntity.ok(articleService.create(dto, SpringSecurityUtil.getCurrentUser().getId()));
    }

    @PutMapping("/adm/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR','PUBLISHER')")
    public ResponseEntity<ArticleDTO> update(@RequestBody ArticleCreateDTO dto,
                                             @PathVariable String id) {
        log.warn("Article update id {} ", id);
        return ResponseEntity.ok(articleService.updateById(dto, id));
    }

    @DeleteMapping("/adm/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR','PUBLISHER')")
    public ResponseEntity<Boolean> deleteById(@PathVariable String id) {
        log.warn("Article delete id {} ", id);
        return ResponseEntity.ok(articleService.deleteById(id));
    }

    @PutMapping("/adm/changeStatus/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PUBLISHER')")
    public ResponseEntity<Integer> changeStatusById(@PathVariable String id) {
        log.warn("change status id {}", id);
        return ResponseEntity.ok(articleService.changeStatusById(id));
    }

    @GetMapping("/getTypeId")
    public ResponseEntity<?> getLastArticleByType(@RequestParam(value = "typeId", defaultValue = "1") Long typeId,
                                                  @RequestParam(value = "size", defaultValue = "3") Integer size) {
        return ResponseEntity.ok(articleService.getLastArticleByType(typeId, size));
    }

    @PostMapping("/notListed")
    public ResponseEntity<List<ArticleShortInfoDTO>> getTheLastArticleNotListed(@RequestBody GetTheLastArticleNotListedDTO dto) {
        return ResponseEntity.ok(articleService.getTheLastArticleNotListed(dto));
    }

    @GetMapping("/getIdAndLang")
    public ResponseEntity<List<ArticleDTO>> getByIdAndLang(@RequestParam(value = "id") String id,
                                                           @RequestParam(value = "lang") String lang) {
        return ResponseEntity.ok(articleService.getByIdAndLang(id, lang));
    }

    @PostMapping("/increaseViewCount/{id}")
    public ResponseEntity<Integer> increaseViewCount(@PathVariable String id) {
        return ResponseEntity.ok(articleService.increaseViewCount(id));
    }

    @PostMapping("/increaseShareViewCount/{id}")
    public ResponseEntity<Integer> increaseShareViewCount(@PathVariable String id) {
        return ResponseEntity.ok(articleService.increaseShareViewCount(id));
    }

    @GetMapping("/getMostRead")
    public ResponseEntity<List<ArticleShortInfoDTO>> getMostRead(@RequestParam(value = "size", defaultValue = "4") Integer size) {
        return ResponseEntity.ok(articleService.getMostRead(size));
    }

    @GetMapping("/getTypeAndByRegion")
    public ResponseEntity<List<ArticleShortInfoDTO>> getTypeAndByRegion(@RequestParam(value = "typeId") Long typeId,
                                                                        @RequestParam(value = "regionId") Integer regionId) {
        return ResponseEntity.ok(articleService.getTypeAndByRegion(typeId, regionId));
    }

    @PostMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<PageImpl<ArticleShortInfoDTO>> filter(@RequestBody ArticleFilterDTO dto,
                                                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                @RequestParam(value = "size", defaultValue = "5") Integer size) {
        return ResponseEntity.ok(articleService.filter(dto, page - 1, size));
    }


}
