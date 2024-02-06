package com.example.service;

import com.example.dto.ArticleCreateDTO;
import com.example.dto.ArticleDTO;
import com.example.dto.ArticleShortInfoDTO;
import com.example.entity.ArticleEntity;
import com.example.entity.ArticleNewsTypeEntity;
import com.example.exp.AppBadException;
import com.example.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleNewsTypeService articleNewsTypeService;


    public ArticleDTO create(ArticleCreateDTO dto, Integer profileId) {
        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setPhotoId(dto.getPhotoId());
        entity.setRegionId(dto.getRegionId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setModeratorId(profileId);
        articleRepository.save(entity);
        articleNewsTypeService.create(entity.getId(), dto.getArticleType());
        return toDTO(entity);
    }

    public ArticleDTO toDTO(ArticleEntity entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setImagesId(entity.getPhotoId());
        dto.setPublishedId(entity.getPublisherId());
        return dto;
    }

    public ArticleDTO updateById(ArticleCreateDTO dto, String id) {
        ArticleEntity entity = get(id);
        entity.setTitle(dto.getTitle() != null ? dto.getTitle() : entity.getTitle());
        entity.setDescription(dto.getDescription() != null ? dto.getDescription() : entity.getDescription());
        entity.setCategoryId(dto.getCategoryId() != null ? dto.getCategoryId() : entity.getCategoryId());
        entity.setRegionId(dto.getRegionId() != null ? dto.getRegionId() : entity.getRegionId());
        entity.setPhotoId(dto.getPhotoId() != null ? dto.getPhotoId() : entity.getPhotoId());
        entity.setContent(dto.getContent() != null ? dto.getContent() : entity.getContent());
        articleRepository.save(entity);
        articleNewsTypeService.merge(entity.getId(), dto.getArticleType());
        return toDTO(entity);
    }

    private ArticleEntity get(String id) {
        Optional<ArticleEntity> optionalArticleEntity = articleRepository.findById(id);
        if (optionalArticleEntity.isEmpty()) {
            throw new AppBadException("Article not found");
        }
        return optionalArticleEntity.get();
    }

}
