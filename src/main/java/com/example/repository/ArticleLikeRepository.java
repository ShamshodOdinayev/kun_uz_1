package com.example.repository;

import com.example.entity.ArticleLikeEntity;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ArticleLikeRepository extends CrudRepository<ArticleLikeEntity, Integer> {
    @Transactional
    @Modifying
    @Query("update ArticleLikeEntity set visible=false where id=?1")
    void deleteById(@NotNull Integer id);

    Optional<ArticleLikeEntity> findByArticleIdAndProfileId(String articleId, Integer profileId);
}
