package com.example.service;

import com.example.dto.EmailSendHistoryDTO;
import com.example.entity.EmailSendHistoryEntity;
import com.example.repository.EmailSendHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class EmailSendHistoryService {
    @Autowired
    private EmailSendHistoryRepository emailSendHistoryRepository;

    public void saveHistory(String message, String email) {
        EmailSendHistoryEntity entity = new EmailSendHistoryEntity();
        entity.setEmail(email);
        entity.setMessage(message);
        emailSendHistoryRepository.save(entity);
    }

    public List<EmailSendHistoryDTO> get(String email) {
        List<EmailSendHistoryEntity> entityList = emailSendHistoryRepository.findByEmail(email);
        List<EmailSendHistoryDTO> dtoList = new LinkedList<>();
        for (EmailSendHistoryEntity entity : entityList) {
            dtoList.add(toDTO(entity));
        }
        return dtoList;

    }

    private static EmailSendHistoryDTO toDTO(EmailSendHistoryEntity entity) {
        EmailSendHistoryDTO dto = new EmailSendHistoryDTO();
        dto.setId(entity.getId());
        dto.setMessage(entity.getMessage());
        dto.setEmail(entity.getEmail());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public List<EmailSendHistoryDTO> getByGiven(LocalDate date) {
        LocalDateTime fromDate = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime toDate = LocalDateTime.of(date, LocalTime.MAX);
        List<EmailSendHistoryEntity> entityList = emailSendHistoryRepository.getByGiven(fromDate, toDate);
        List<EmailSendHistoryDTO> dtoList = new LinkedList<>();
        for (EmailSendHistoryEntity entity : entityList) {
            dtoList.add(toDTO(entity));
        }
        return dtoList;
    }
}
