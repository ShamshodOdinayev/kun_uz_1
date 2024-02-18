package com.example.repository;

import com.example.dto.ArticleFilterDTO;
import com.example.dto.PaginationResultDTO;
import com.example.entity.ArticleEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ArticleCustomRepository {
    private final EntityManager entityManager;

    public ArticleCustomRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public PaginationResultDTO<ArticleEntity> filter(ArticleFilterDTO filterDTO, Integer page, Integer size) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        if (filterDTO.getStatus() != null) {
            builder.append(" and id=:id");
            params.put("id", filterDTO.getId());
        }
        if (filterDTO.getStatus() != null) {
            builder.append(" and status=:status");
            params.put("status", filterDTO.getStatus());
        }
        if (filterDTO.getCategoryId() != null) {
            builder.append(" and categoryId=:categoryId");
            params.put("categoryId", filterDTO.getCategoryId());
        }
        if (filterDTO.getRegionId() != null) {
            builder.append(" and regionId=:regionId");
            params.put("regionId", filterDTO.getRegionId());
        }
        if (filterDTO.getTitle() != null) {
            builder.append(" and title=:title");
            params.put("title", filterDTO.getTitle());
        }
        if (filterDTO.getModeratorId() != null) {
            builder.append(" and moderatorId=:moderatorId");
            params.put("moderatorId", filterDTO.getModeratorId());
        }
        if (filterDTO.getPublisherId() != null) {
            builder.append(" and publishedId=:publishedId");
            params.put("publishedId", filterDTO.getPublisherId());
        }
        if (filterDTO.getToDate() != null && filterDTO.getFromDate() != null) {
            LocalDateTime fromDate = LocalDateTime.of(filterDTO.getFromDate(), LocalTime.MIN);
            LocalDateTime toDate = LocalDateTime.of(filterDTO.getToDate(), LocalTime.MAX);
            builder.append(" and createdDate between :fromDate and :toDate ");
            params.put("fromDate", fromDate);
            params.put("toDate", toDate);
        } else if (filterDTO.getFromDate() != null) {
            LocalDateTime fromDate = LocalDateTime.of(filterDTO.getFromDate(), LocalTime.MIN);
            LocalDateTime toDate = LocalDateTime.of(filterDTO.getFromDate(), LocalTime.MAX);
            builder.append(" and createdDate between  :fromDate and :toDate ");
            params.put("fromDate", fromDate);
            params.put("toDate", toDate);
        } else if (filterDTO.getToDate() != null) {
            LocalDateTime toDate = LocalDateTime.of(filterDTO.getToDate(), LocalTime.MAX);
            builder.append(" and createdDate <= :toDate");
            params.put("toDate", toDate);
        }
        if (filterDTO.getToPublishedDate() != null && filterDTO.getFromPublishedDate() != null) {
            LocalDateTime fromPublishedDate = LocalDateTime.of(filterDTO.getFromPublishedDate(), LocalTime.MIN);
            LocalDateTime toPublishedDate = LocalDateTime.of(filterDTO.getToPublishedDate(), LocalTime.MAX);
            builder.append(" and publishedDate between :fromPublishedDate and :toPublishedDate ");
            params.put("fromPublishedDate", fromPublishedDate);
            params.put("toPublishedDate", toPublishedDate);
        } else if (filterDTO.getFromPublishedDate() != null) {
            LocalDateTime fromPublishedDate = LocalDateTime.of(filterDTO.getFromPublishedDate(), LocalTime.MIN);
            LocalDateTime toPublishedDate = LocalDateTime.of(filterDTO.getFromPublishedDate(), LocalTime.MAX);
            builder.append(" and publishedDate between  :fromPublishedDate and :toPublishedDate ");
            params.put("fromPublishedDate", fromPublishedDate);
            params.put("toPublishedDate", toPublishedDate);
        } else if (filterDTO.getToPublishedDate() != null) {
            LocalDateTime toPublishedDate = LocalDateTime.of(filterDTO.getToPublishedDate(), LocalTime.MAX);
            builder.append(" and publishedDate <= :toPublishedDate");
            params.put("toPublishedDate", toPublishedDate);
        }
        String counterBuilder = "select count(c) from ArticleEntity c where 1=1 " + builder;
        Query selectQuery = entityManager.createQuery("from ArticleEntity c where 1=1 " + builder + " order by createdDate desc ");
        selectQuery.setMaxResults(size);
        selectQuery.setFirstResult(page * size);
        Query countQuery = entityManager.createQuery(counterBuilder);
        for (Map.Entry<String, Object> param : params.entrySet()) {
            selectQuery.setParameter(param.getKey(), param.getValue());
            countQuery.setParameter(param.getKey(), param.getValue());
        }
        List<ArticleEntity> entityList = selectQuery.getResultList();
        Long totalElements = (Long) countQuery.getSingleResult();
        return new PaginationResultDTO<>(totalElements, entityList);
    }
}
