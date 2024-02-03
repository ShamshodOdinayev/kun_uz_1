package com.example.controller;

import com.example.dto.ArticleCreateDTO;
import com.example.dto.ArticleDTO;
import com.example.dto.ArticleShortInfoDTO;
import com.example.enums.ProfileRole;
import com.example.service.ArticleService;
import com.example.util.HttpRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping("/adm")
    public ResponseEntity<ArticleDTO> create(@RequestBody ArticleCreateDTO dto,
                                              HttpServletRequest request
    ) {
        Integer profileId = HttpRequestUtil.getProfileId(request, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.create(dto, profileId));
    }

    @PutMapping("/adm/{id}")
    public ResponseEntity<ArticleDTO> update(@RequestBody ArticleCreateDTO dto,
                                             @PathVariable String id,
                                              HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.MODERATOR, ProfileRole.PUBLISHER);
        return ResponseEntity.ok(articleService.updateById(dto, id));
    }

    @GetMapping("/adm")
    public ResponseEntity<ArticleShortInfoDTO> getLastArticleByType(@RequestParam(value = "typeId",defaultValue = "1") String typeId,
                                                                    @RequestParam(value = "size",defaultValue = "3") Integer size){
        return ResponseEntity.ok(articleService.getLastArticleByType(typeId, size));
    }


}
