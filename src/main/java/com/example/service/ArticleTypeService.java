package com.example.service;

import com.example.dto.ArticleTypeCrudeDTO;
import com.example.dto.ArticleTypeDTO;
import com.example.dto.GetByLangDTO;
import com.example.entity.ArticleTypeEntity;
import com.example.enums.AppLanguage;
import com.example.exp.AppBadException;
import com.example.repository.ArticleTypeRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleTypeService {
    private final ArticleTypeRepository articleTypeRepository;

    public ArticleTypeService(ArticleTypeRepository articleTypeRepository) {
        this.articleTypeRepository = articleTypeRepository;
    }

    public Boolean create(ArticleTypeCrudeDTO dto) {
        ArticleTypeEntity entity = dtoToEntity(dto);
        articleTypeRepository.save(entity);
        return true;
    }

    public ArticleTypeDTO updateById(Integer id, ArticleTypeCrudeDTO dto) {
        ArticleTypeEntity updateEntity = checkByIdEntity(id);
        if (dto.getOrderNumber() == null || dto.getNameUz() == null || dto.getNameRu() == null || dto.getNameEn() == null) {
            throw new AppBadException("OrderNumber or NameUz or NameRu or NameEn not available");
        }
        updateEntity.setOrderNumber(dto.getOrderNumber());
        updateEntity.setNameEn(dto.getNameEn());
        updateEntity.setNameRu(dto.getNameRu());
        updateEntity.setNameUz(dto.getNameUz());
        articleTypeRepository.save(updateEntity);
        return entityToDTO(updateEntity);
    }

    public Boolean deleteById(Integer id) {
        checkByIdEntity(id);
        articleTypeRepository.deleteById(id);
        return true;
    }

    public List<ArticleTypeDTO> getAll() {
        Iterable<ArticleTypeEntity> entityIterable = articleTypeRepository.findAll();
        List<ArticleTypeDTO> dtoList = new LinkedList<>();
        for (ArticleTypeEntity entity : entityIterable) {
            ArticleTypeDTO dto = entityToDTO(entity);
            dto.setCreatedDate(entity.getCreatedDate());
            dto.setVisible(entity.getVisible());
            dtoList.add(dto);
        }
        return dtoList;
    }

    public List<GetByLangDTO> getByLang(AppLanguage lang) {
        List<ArticleTypeDTO> articleTypeDTOS = getAll();
        List<GetByLangDTO> dtoList = new LinkedList<>();
        for (ArticleTypeDTO articleTypeDTO : articleTypeDTOS) {
            if (!articleTypeDTO.getVisible()) {
                continue;
            }
            GetByLangDTO dto = new GetByLangDTO();
            dto.setId(articleTypeDTO.getId());
            switch (lang) {
                case uz -> dto.setName(articleTypeDTO.getNameUz());
                case ru -> dto.setName(articleTypeDTO.getNameRu());
                default -> dto.setName(articleTypeDTO.getNameEn());
            }
            dtoList.add(dto);
        }
        return dtoList;
    }

    private ArticleTypeEntity checkByIdEntity(Integer id) {
        Optional<ArticleTypeEntity> entity = articleTypeRepository.findById(id);
        if (entity.isEmpty()) {
            throw new AppBadException("ArticleType not found");
        }
        return entity.get();
    }

    private static ArticleTypeDTO entityToDTO(ArticleTypeEntity updateEntity) {
        ArticleTypeDTO articleTypeDTO = new ArticleTypeDTO();
        articleTypeDTO.setId(updateEntity.getId());
        articleTypeDTO.setOrderNumber(updateEntity.getOrderNumber());
        articleTypeDTO.setNameUz(updateEntity.getNameUz());
        articleTypeDTO.setNameRu(updateEntity.getNameRu());
        articleTypeDTO.setNameEn(updateEntity.getNameEn());
        return articleTypeDTO;
    }

    private static ArticleTypeEntity dtoToEntity(ArticleTypeCrudeDTO dto) {
        ArticleTypeEntity entity = new ArticleTypeEntity();
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        return entity;
    }

}
