package com.example.repository;

import com.example.entity.ArticleNewsTypeEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleNewsTypeRepository extends CrudRepository<ArticleNewsTypeEntity, Integer> {
    List<ArticleNewsTypeEntity> findByArticleIdAndVisible(String articleId, Boolean visible);

    @Transactional
    @Modifying
    @Query("delete from ArticleNewsTypeEntity where typesId=?1 and articleId=?2")
    void deleteByTypesId(Integer typesId, String articleId);

    @Transactional
    @Modifying
    @Query("update ArticleNewsTypeEntity set visible=false where articleId=?1 and typesId=?2")
    void updateByArticleIdAndTypesId(String articleId, Integer typesId);

    List<ArticleNewsTypeEntity> findByTypesIdAndVisibleOrderByCreatedDate(Integer typesId, Boolean visible);


}
