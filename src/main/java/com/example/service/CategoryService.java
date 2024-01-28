package com.example.service;

import com.example.dto.CategoryCreateDTO;
import com.example.dto.CategoryDTO;
import com.example.dto.GetByLangDTO;
import com.example.dto.RegionDTO;
import com.example.entity.CategoryEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.AppLanguage;
import com.example.exp.AppBadException;
import com.example.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryCreateDTO create(CategoryCreateDTO dto) {
        if (dto.getOrderNumber() == null || dto.getNameUz() == null || dto.getNameRu() == null || dto.getNameEn() == null) {
            throw new AppBadException("Some parameter is null. All fields must be given a value!!!");
        }
        CategoryEntity entity = new CategoryEntity();
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        categoryRepository.save(entity);
        return dto;
    }

    public Boolean updateById(Integer id, CategoryCreateDTO dto) {
        Optional<CategoryEntity> entityOptional = categoryRepository.findById(id);
        if (entityOptional.isEmpty()) {
            return false;
        }
        CategoryEntity entity = entityOptional.get();
        entity.setOrderNumber(dto.getOrderNumber() == null ? entity.getOrderNumber() : dto.getOrderNumber());
        entity.setNameRu(dto.getNameRu() == null ? entity.getNameRu() : dto.getNameRu());
        entity.setNameEn(dto.getNameEn() == null ? entity.getNameEn() : dto.getNameEn());
        entity.setNameUz(dto.getNameUz() == null ? entity.getNameUz() : dto.getNameUz());
        entity.setUpdatedDate(LocalDateTime.now());
        categoryRepository.save(entity);
        return true;
    }

    public Boolean deleteById(Integer id) {
        Optional<CategoryEntity> entityOptional = categoryRepository.findById(id);
        if (entityOptional.isEmpty()) {
            throw new AppBadException("No user found with this id");
        }
        categoryRepository.deleteByIdQuery(id);
        return true;
    }

    public List<CategoryDTO> getAll() {
        List<CategoryDTO> dtoList = new LinkedList<>();
        Iterable<CategoryEntity> entityList = categoryRepository.findAll();
        for (CategoryEntity entity : entityList) {
            dtoList.add(toDTO(entity));
        }
        return dtoList;
    }

    private static CategoryDTO toDTO(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setNameEn(entity.getNameEn());
        dto.setNameUz(entity.getNameUz());
        dto.setNameRu(entity.getNameRu());
        dto.setVisible(entity.getVisible());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public List<GetByLangDTO> getByLang(AppLanguage lang) {
        List<CategoryEntity> entityList = categoryRepository.findAllByVisible(true);
        List<GetByLangDTO> dtoList = new LinkedList<>();
        for (CategoryEntity entity : entityList) {
            GetByLangDTO dto = new GetByLangDTO();
            dto.setId(entity.getId());
            dto.setOrderNumber(entity.getOrderNumber());
            switch (lang) {
                case uz -> dto.setName(entity.getNameUz());
                case ru -> dto.setName(entity.getNameRu());
                default -> dto.setName(entity.getNameEn());
            }
            dtoList.add(dto);
        }
        return dtoList;
    }
}
