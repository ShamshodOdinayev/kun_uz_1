package com.example.controller;

import com.example.service.ArticleLikeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "ArticleLike API list")
@RestController
@RequestMapping("/articleLike")
public class ArticleLikeController {
    @Autowired
    private ArticleLikeService articleLikeService;

    @PostMapping("/like/{articleId}")
    public ResponseEntity<Boolean> like(@PathVariable(value = "articleId") String articleId) {
        return ResponseEntity.ok(articleLikeService.like(articleId));
    }

    @PostMapping("/dislike/{articleId}")
    public ResponseEntity<Boolean> dislike(@PathVariable String articleId) {
        return ResponseEntity.ok(articleLikeService.dislike(articleId));
    }

    @PostMapping("/remove/{articleId}")
    public ResponseEntity<Boolean> remove(@PathVariable String articleId) {
        return ResponseEntity.ok(articleLikeService.remove(articleId));
    }


}
