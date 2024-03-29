package com.example.service;

import com.example.dto.*;
import com.example.entity.ArticleEntity;
import com.example.entity.ArticleNewsTypeEntity;
import com.example.entity.RegionEntity;
import com.example.enums.ArticleStatus;
import com.example.exp.AppBadException;
import com.example.repository.ArticleCustomRepository;
import com.example.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleNewsTypeService articleNewsTypeService;
    @Autowired
    private AttachService attachService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private ArticleCustomRepository articleCustomRepository;


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

    public ArticleEntity get(String id) {
        Optional<ArticleEntity> optionalArticleEntity = articleRepository.findById(id);
        if (optionalArticleEntity.isEmpty()) {
            throw new AppBadException("Article not found");
        }
        return optionalArticleEntity.get();
    }

    public Boolean deleteById(String id) {
        get(id);
        articleRepository.deleteById(id);
        return true;
    }

    public Integer changeStatusById(String id) {
        ArticleEntity entity = get(id);
        if (entity.getStatus().equals(ArticleStatus.PUBLISHED)) {
            return articleRepository.changeStatusById(id, ArticleStatus.NOT_PUBLISHED);
        }
        return articleRepository.changeStatusById(id, ArticleStatus.PUBLISHED);
    }

    public List<ArticleDTO> getLastArticleByType(Long typeId, int size) {
        List<ArticleNewsTypeEntity> articleByType = articleNewsTypeService.getLastArticleByType(typeId);
        List<ArticleEntity> entitySet = new LinkedList<>();
        List<ArticleDTO> dtoList = new LinkedList<>();
        for (int i = 0; i < Math.min(articleByType.size(), size); i++) {
            entitySet.add(articleRepository.findById(articleByType.get(i).getArticleId()).get());
        }
        for (ArticleEntity entity : entitySet) {
            dtoList.add(toDTO(entity));
        }
        return dtoList;
    }

    public List<ArticleShortInfoDTO> getTheLastArticleNotListed(GetTheLastArticleNotListedDTO dto) {
        List<ArticleShortInfoDTO> shortInfoDTOList = new LinkedList<>();
        String[] articles = dto.getArticles();
        Integer size = dto.getSize();
        List<ArticleEntity> entityList = articleRepository.findAllByOrderByCreatedDate();
//        List<ArticleDTO> shortInfoDTOList = new LinkedList<>();
        int count = 0;
        for (ArticleEntity entity : entityList) {
            for (String id : articles) {
                if (entity.getId().equals(id)) {
                    count++;
                }
            }
            if (count == 0) {
                if (shortInfoDTOList.size() >= size) {
                    return shortInfoDTOList;
                }
                shortInfoDTOList.add(toShortInfoDTO(entity));
            }
            count = 0;
        }
        return shortInfoDTOList;
    }

    public ArticleShortInfoDTO toShortInfoDTO(ArticleEntity entity) {
        ArticleShortInfoDTO dto = new ArticleShortInfoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setImage(attachService.toDTO(entity.getPhoto()));
        dto.setPublishedDate(entity.getPublishedDate());
        return dto;
    }

    public List<ArticleDTO> getByIdAndLang(String id, String lang) {
        //TODO :
        return null;
    }

    public Integer increaseViewCount(String id) {
        ArticleEntity entity = get(id);
        int viewCount = entity.getViewCount() == null ? 0 : entity.getViewCount();
        entity.setViewCount(viewCount + 1);
        articleRepository.save(entity);
        return viewCount + 1;
    }

    public Integer increaseShareViewCount(String id) {
        ArticleEntity entity = get(id);
        int sharedCount = entity.getSharedCount() == null ? 0 : entity.getSharedCount();
        entity.setSharedCount(sharedCount + 1);
        articleRepository.save(entity);
        return sharedCount + 1;
    }

    public List<ArticleShortInfoDTO> getMostRead(int size) {
        List<ArticleEntity> entityList = articleRepository.getMostRead(size);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        for (ArticleEntity entity : entityList) {
            dtoList.add(toShortInfoDTO(entity));
        }
        return dtoList;
    }

    public List<ArticleShortInfoDTO> getTypeAndByRegion(Long typeId, Integer regionId) {
        List<ArticleNewsTypeEntity> articleByType = articleNewsTypeService.getLastArticleByType(typeId);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        RegionEntity region = regionService.get(regionId);
        for (ArticleNewsTypeEntity articleNewsTypeEntity : articleByType) {
            Optional<ArticleEntity> allByIdAndRegion = articleRepository.findAllByIdAndRegionAndVisible(articleNewsTypeEntity.getArticleId(), region, true);
            allByIdAndRegion.ifPresent(entity -> dtoList.add(toShortInfoDTO(entity)));
        }
        return dtoList;
    }

    public PageImpl<ArticleShortInfoDTO> filter(ArticleFilterDTO dto, Integer page, Integer size) {
        PaginationResultDTO<ArticleEntity> paginationResultDTO = articleCustomRepository.filter(dto, page, size);
        List<ArticleEntity> articleEntityList = paginationResultDTO.getList();
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        for (ArticleEntity entity : articleEntityList) {
            dtoList.add(toShortInfoDTO(entity));
        }
        Pageable pageable = PageRequest.of(page, size);
        return new PageImpl<>(dtoList, pageable, paginationResultDTO.getTotalSize());
    }
}
