package com.example.repository;

import com.example.dto.PaginationResultDTO;
import com.example.dto.ProfileFilterDTO;
import com.example.entity.ProfileEntity;
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
public class ProfileCustomRepository {
    @Autowired
    private EntityManager entityManager;

    public PaginationResultDTO<ProfileEntity> filter(ProfileFilterDTO filterDTO, Integer page, Integer size) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        if (filterDTO.getRole() != null) {
            builder.append(" and role=:role");
            params.put("role", filterDTO.getRole());
        }
        if (filterDTO.getName() != null) {
            builder.append(" and lower(name) like :name");
            params.put("name", "%" + filterDTO.getName() + "%");
        }
        if (filterDTO.getSurname() != null) {
            builder.append(" and lower(surname) like :surname");
            params.put("surname", "%" + filterDTO.getSurname() + "%");
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
        String counterBuilder = "select count(p) from ProfileEntity p where 1=1 " + builder;
        Query selectQuery = entityManager.createQuery("from ProfileEntity c where 1=1 " + builder + " order by createdDate desc ");
        selectQuery.setMaxResults(size);
        selectQuery.setFirstResult(page * size);
        Query countQuery = entityManager.createQuery(counterBuilder);
        for (Map.Entry<String, Object> param : params.entrySet()) {
            selectQuery.setParameter(param.getKey(), param.getValue());
            countQuery.setParameter(param.getKey(), param.getValue());
        }
        List<ProfileEntity> entityList = selectQuery.getResultList();
        Long totalElements = (Long) countQuery.getSingleResult();
        return new PaginationResultDTO<>(totalElements, entityList);
    }
}
