package com.example.service;

import com.example.dto.ArticleCreateDTO;
import com.example.dto.ArticleDTO;
import com.example.dto.ArticleShortInfoDTO;
import com.example.dto.ArticleTypeCrudeDTO;
import com.example.entity.*;
import com.example.enums.ArticleStatus;
import com.example.exp.AppBadException;
import com.example.repository.ArticleRepository;
import com.example.repository.CategoryRepository;
import com.example.repository.ProfileRepository;
import com.example.repository.RegionRepository;
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
    private RegionRepository regionRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ArticleTypeService articleTypeService;

    public ArticleDTO create(ArticleCreateDTO dto, Integer profileId) {
        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setImagesId(dto.getImageId());
        Optional<RegionEntity> regionEntity = regionRepository.findById(dto.getRegionId());
        if (regionEntity.isEmpty()) {
            throw new AppBadException("Region not found");
        }
        entity.setRegion(regionEntity.get());
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(dto.getCategoryId());
        if (categoryEntity.isEmpty()) {
            throw new AppBadException("Category not found");
        }
        entity.setCategory(categoryEntity.get());
        List<ArticleTypeCrudeDTO> articleTypeCrudeDTOList = dto.getArticleType();
        entity.setStatus(ArticleStatus.NOT_PUBLISHED);
        Optional<ProfileEntity> profileEntityOptional = profileRepository.findById(profileId);
        if (profileEntityOptional.isEmpty()) {
            throw new AppBadException("Moderator not found");
        }
        entity.setModerator(profileEntityOptional.get());
        articleRepository.save(entity);
        return toDTO(entity);
    }

    public ArticleDTO toDTO(ArticleEntity entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setCategory(entity.getCategory());
        dto.setModerator(entity.getModerator());
        dto.setRegion(entity.getRegion());
        dto.setContent(entity.getContent());
        dto.setImageId(entity.getImagesId());
        dto.setStatus(entity.getStatus());
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setPublisher(entity.getPublisher());
        dto.setViewCount(entity.getViewCount());
        dto.setSharedCount(entity.getSharedCount());
        return dto;
    }

    public ArticleDTO updateById(ArticleCreateDTO dto, String id) {
        Optional<ArticleEntity> entityOptional = articleRepository.findById(id);
        if (entityOptional.isEmpty()) {
            throw new AppBadException("article not found");
        }
        ArticleEntity entity = entityOptional.get();
        entity.setTitle(check(dto.getTitle()) ? dto.getTitle() : entity.getTitle());
        entity.setDescription(check(dto.getDescription()) ? dto.getDescription() : entity.getDescription());
        entity.setContent(check(dto.getContent()) ? dto.getContent() : entity.getContent());
        entity.setImagesId(check(dto.getImageId()) ? dto.getImageId() : entity.getImagesId());
        Optional<RegionEntity> regionEntity = regionRepository.findById(check(dto.getRegionId()) ? dto.getRegionId() : entity.getRegion().getId());
        if (regionEntity.isEmpty()) {
            throw new AppBadException("Region not found");
        }
        entity.setRegion(regionEntity.get());
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(check(dto.getCategoryId()) ? dto.getCategoryId() : entity.getCategory().getId());
        if (categoryEntity.isEmpty()) {
            throw new AppBadException("Category not found");
        }
        entity.setCategory(categoryEntity.get());
        articleRepository.save(entity);
        return toDTO(entity);
    }

    public Boolean check(Object o) {
        return o != null;
    }

    public ArticleShortInfoDTO getLastArticleByType(String typeId, Integer size) {
        return null;
    }
}
