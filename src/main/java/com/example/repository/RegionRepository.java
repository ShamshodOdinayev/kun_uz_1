package com.example.repository;

import com.example.entity.RegionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RegionRepository extends CrudRepository<RegionEntity, Integer> {
    @Transactional
    @Modifying
    @Query("update RegionEntity set visible=false where id=?1")
    void deleteById(Integer id);
}
