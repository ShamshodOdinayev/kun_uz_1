package com.example.repository;

import com.example.entity.CommentLikeEntity;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CommentLikeRepository extends CrudRepository<CommentLikeEntity, Integer> {

    Optional<CommentLikeEntity> findByCommentIdAndProfileId(Integer commentId, Integer profileId);

    @Transactional
    @Modifying
    @Query("update CommentLikeEntity set visible=false where id=?1")
    void remove(@NotNull Integer id);


}
