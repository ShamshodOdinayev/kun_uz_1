package com.example.repository;

import com.example.entity.ArticleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleRepository extends CrudRepository<ArticleEntity, String> {
  /*  @Query("FROM ArticleEntity where  ")
    List<ArticleEntity> findByType(String type, Integer size);*/

}
