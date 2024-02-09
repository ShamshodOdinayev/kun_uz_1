package com.example.controller;

import com.example.dto.ArticleCreateDTO;
import com.example.dto.ArticleDTO;
import com.example.dto.ArticleShortInfoDTO;
import com.example.dto.GetTheLastArticleNotListedDTO;
import com.example.service.ArticleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ArticleDTO> create(@RequestBody ArticleCreateDTO dto,
                                             HttpServletRequest request) {
        log.warn("Article create");
        return ResponseEntity.ok(articleService.create(dto, 2));
    }

    @PutMapping("/adm/{id}")
    public ResponseEntity<ArticleDTO> update(@RequestBody ArticleCreateDTO dto,
                                             @PathVariable String id,
                                             HttpServletRequest request) {
        log.warn("Article update id {} ", id);
        return ResponseEntity.ok(articleService.updateById(dto, id));
    }

    @DeleteMapping("/adm/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable String id) {
        log.warn("Article delete id {} ", id);
        return ResponseEntity.ok(articleService.deleteById(id));
    }

    @PutMapping("/adm/changeStatus/{id}")
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


}
