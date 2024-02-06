package com.example.repository;

import com.example.entity.ArticleEntity;
import com.example.entity.ArticleNewsTypeEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleNewsTypeRepository extends CrudRepository<ArticleNewsTypeEntity, Integer> {
    List<ArticleNewsTypeEntity> findByArticleId(String articleId);

    @Transactional
    @Modifying
    @Query("delete from ArticleNewsTypeEntity where typesId=?1 and articleId=?2")
    void deleteByTypesId(Integer typesId, String articleId);


   /* @Transactional
    @Modifying
    @Query("delete from ArticleNewsTypeEntity where ty")
    void deleteSameTypeId(String articleId);*/
}
