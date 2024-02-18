package com.example.repository;

import com.example.dto.CommentFilterDTO;
import com.example.dto.PaginationResultDTO;
import com.example.entity.CommentEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CommentCustomRepository {
    private final EntityManager entityManager;

    public CommentCustomRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public PaginationResultDTO<CommentEntity> filter(CommentFilterDTO filterDTO, Integer page, Integer size) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        if (filterDTO.getVisible() != null) {
            builder.append(" and visible=:visible");
            params.put("visible", filterDTO.getVisible());
        }
        if (filterDTO.getContent() != null) {
            builder.append(" and lower(content) like :content");
            params.put("content", "%" + filterDTO.getContent().toLowerCase() + "%");
        }
        if (filterDTO.getProfileId() != null) {
            builder.append(" and profileId=:profileId");
            params.put("profileId", filterDTO.getProfileId());
        }
        if (filterDTO.getReplyId() != null) {
            builder.append(" and replyId=:replyId");
            params.put("replyId", filterDTO.getReplyId());
        }
        if (filterDTO.getArticleId() != null) {
            builder.append(" and articleId=:articleId");
            params.put("articleId", filterDTO.getArticleId());
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
        String counterBuilder = "select count(c) from CommentEntity c where 1=1 " + builder;
        Query selectQuery = entityManager.createQuery("from CommentEntity c where 1=1 " + builder + " order by createdDate desc ");
        selectQuery.setMaxResults(size);
        selectQuery.setFirstResult(page * size);
        Query countQuery = entityManager.createQuery(counterBuilder);
        for (Map.Entry<String, Object> param : params.entrySet()) {
            selectQuery.setParameter(param.getKey(), param.getValue());
            countQuery.setParameter(param.getKey(), param.getValue());
        }
        List<CommentEntity> entityList = selectQuery.getResultList();
        Long totalElements = (Long) countQuery.getSingleResult();
        return new PaginationResultDTO<>(totalElements, entityList);
    }
}
