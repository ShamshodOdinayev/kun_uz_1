package com.example.repository;

import com.example.entity.ArticleEntity;
import com.example.entity.RegionEntity;
import com.example.enums.ArticleStatus;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends CrudRepository<ArticleEntity, String> {
    @Transactional
    @Modifying
    @Query("update ArticleEntity set visible=false where id=?1")
    void deleteById(@NotNull String id);

    @Transactional
    @Modifying
    @Query("update ArticleEntity set status=?2 where id=?1")
    Integer changeStatusById(String id, ArticleStatus status);

    @Query("from ArticleEntity where visible=true order by createdDate")
    List<ArticleEntity> findAllByOrderByCreatedDate();

    @Query("from ArticleEntity where visible=true order by viewCount limit ?1")
    List<ArticleEntity> getMostRead(int size);

    Optional<ArticleEntity> findAllByIdAndRegionAndVisible(String id, RegionEntity region, Boolean visible);
}
