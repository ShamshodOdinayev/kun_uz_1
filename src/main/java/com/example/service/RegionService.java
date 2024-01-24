package com.example.service;

import com.example.dto.RegionDTO;
import com.example.dto.GetByLangDTO;
import com.example.entity.RegionEntity;
import com.example.exp.AppBadException;
import com.example.repository.RegionRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class RegionService {
    private final RegionRepository regionRepository;

    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    public Boolean create(RegionDTO dto) {
        checkByVariable(dto);
        RegionEntity entity = toEntity(dto);
        regionRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return true;
    }

    public RegionDTO updateById(RegionDTO dto, Integer id) {
        RegionEntity updateEntity = checkByIdEntity(id);
        checkByVariable(dto);
        updateEntity.setOrderNumber(dto.getOrderNumber());
        updateEntity.setNameEn(dto.getNameEn());
        updateEntity.setNameRu(dto.getNameRu());
        updateEntity.setNameUz(dto.getNameUz());
        regionRepository.save(updateEntity);
        return entityToDTO(updateEntity);
    }

    public Boolean deleteById(Integer id) {
        regionRepository.delete(checkByIdEntity(id));
        /* TODO
         *   visible false qilish kk*/
        return true;
    }

    public List<RegionDTO> getAll() {
        Iterable<RegionEntity> entities = regionRepository.findAll();
        List<RegionDTO> dtoList = new LinkedList<>();
        for (RegionEntity entity : entities) {
            RegionDTO dto = entityToDTO(entity);
            dto.setId(entity.getId());
            dto.setCreatedDate(entity.getCreatedDate());
            dto.setVisible(entity.getVisible());
            dtoList.add(dto);
        }
        return dtoList;
    }

    private static RegionDTO entityToDTO(RegionEntity updateEntity) {
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(updateEntity.getId());
        regionDTO.setOrderNumber(updateEntity.getOrderNumber());
        regionDTO.setNameUz(updateEntity.getNameUz());
        regionDTO.setNameRu(updateEntity.getNameRu());
        regionDTO.setNameEn(updateEntity.getNameEn());
        return regionDTO;
    }

    private RegionEntity checkByIdEntity(Integer id) {
        Optional<RegionEntity> entity = regionRepository.findById(id);
        if (entity.isEmpty()) {
            throw new AppBadException("Region not found");
        }
        return entity.get();
    }

    private static void checkByVariable(RegionDTO dto) {
        if (dto.getOrderNumber() == null || dto.getNameUz() == null || dto.getNameRu() == null || dto.getNameEn() == null) {
            throw new AppBadException("OrderNumber or NameUz or NameRu or NameEn not available");
        }
    }

    private static RegionEntity toEntity(RegionDTO dto) {
        RegionEntity entity = new RegionEntity();
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        return entity;
    }

    public List<GetByLangDTO> getByLang(String lang) {
        return switch (lang) {
            case "uz", "en", "ru" -> getRegionGetByLangDTOS(lang);
            default -> throw new AppBadException("Language not found(en,uz,ru)");
        };
    }

    private List<GetByLangDTO> getRegionGetByLangDTOS(String lang) {
        List<RegionDTO> regionDTOS = getAll();
        List<GetByLangDTO> dtoList = new LinkedList<>();
        for (RegionDTO regionDTO : regionDTOS) {
            if (!regionDTO.getVisible()) {
                continue;
            }
            GetByLangDTO dto = new GetByLangDTO();
            dto.setId(regionDTO.getId());
            switch (lang) {
                case "uz" -> dto.setName(regionDTO.getNameUz());
                case "ru" -> dto.setName(regionDTO.getNameRu());
                case "en" -> dto.setName(regionDTO.getNameEn());
            }
            dtoList.add(dto);
        }
        return dtoList;
    }
}
