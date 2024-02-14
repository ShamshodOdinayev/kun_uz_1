package com.example.repository;

import com.example.entity.CommentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CommentRepository extends CrudRepository<CommentEntity, Integer> {
    Optional<CommentEntity> findByIdAndVisible(Integer id, Boolean visible);

}
