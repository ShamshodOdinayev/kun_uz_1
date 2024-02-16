package com.example.repository;

import com.example.entity.CommentEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends CrudRepository<CommentEntity, Integer> {
    Optional<CommentEntity> findByIdAndVisible(Integer id, Boolean visible);

    @Transactional
    @Modifying
    @Query("update CommentEntity set visible=false where id=?1")
    void deleteById(Integer id);
    List<CommentEntity> findByArticleIdAndVisible(String articleId, Boolean visible);
}
