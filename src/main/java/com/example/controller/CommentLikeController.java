package com.example.controller;

import com.example.service.CommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commentLike")
public class CommentLikeController {
    @Autowired
    private CommentLikeService commentLikeService;

    @PostMapping("/like/{commentId}")
    public ResponseEntity<Boolean> like(@PathVariable(value = "commentId") Integer commentId) {
        return ResponseEntity.ok(commentLikeService.like(commentId));
    }

    @PostMapping("/dislike/{commentId}")
    public ResponseEntity<Boolean> dislike(@PathVariable Integer commentId) {
        return ResponseEntity.ok(commentLikeService.dislike(commentId));
    }

    @PostMapping("/remove/{commentId}")
    public ResponseEntity<Boolean> remove(@PathVariable Integer commentId) {
        return ResponseEntity.ok(commentLikeService.remove(commentId));
    }


}
