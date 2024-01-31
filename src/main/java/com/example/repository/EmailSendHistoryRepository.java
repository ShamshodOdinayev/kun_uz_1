package com.example.repository;

import com.example.entity.EmailSendHistoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailSendHistoryRepository extends CrudRepository<EmailSendHistoryEntity, Integer> {
    List<EmailSendHistoryEntity> findByEmail(String email);

    @Query("from EmailSendHistoryEntity where createdDate>=?1 and createdDate<=?2")
    List<EmailSendHistoryEntity> getByGiven(LocalDateTime fromDate, LocalDateTime toDate);


}
