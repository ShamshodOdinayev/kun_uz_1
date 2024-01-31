package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "email_send_history")
public class EmailSendHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "message", columnDefinition = "text")
    private String message;
    @Column(name = "email")
    private String email;
    @Column(name = "created_date")
    protected LocalDateTime createdDate = LocalDateTime.now();

}
