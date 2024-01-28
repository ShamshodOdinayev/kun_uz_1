package com.example.repository;

import com.example.entity.CategoryEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Integer> {
    @Transactional
    @Modifying
    @Query("update CategoryEntity set visible=false where id=?1")
    void deleteByIdQuery(Integer id);

    @Query("from CategoryEntity where visible=?1")
    List<CategoryEntity> findAllByVisible(Boolean visible);
}
