package com.example.controller;

import com.example.exp.AppBadException;
import com.example.exp.ForbiddenException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {
    @ExceptionHandler(AppBadException.class)
    private ResponseEntity<?> handle(AppBadException e) {
        log.warn("AppBadException {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(JwtException.class)
    private ResponseEntity<?> handle(JwtException e) {
        log.warn("JwtException {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(ForbiddenException.class)
    private ResponseEntity<?> handle(ForbiddenException e) {
        log.warn("ForbiddenException {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    private ResponseEntity<?> handle(AccessDeniedException e) {
        log.warn("ForbiddenException {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    private ResponseEntity<?> handle(HttpMessageConversionException e) {
        log.warn("ForbiddenException {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<?> handle(RuntimeException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
