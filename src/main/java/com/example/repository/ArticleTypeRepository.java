package com.example.repository;

import com.example.dto.ArticleTypeDTO;
import com.example.entity.ArticleTypeEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleTypeRepository extends CrudRepository<ArticleTypeEntity, Integer> {
    @Transactional
    @Modifying
    @Query("update ArticleTypeEntity set visible=false where id=?1")
    void deleteById(Integer id);

    List<ArticleTypeDTO> findAllByVisible(boolean visible);
}
 