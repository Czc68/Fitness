package com.fitness.booking.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice(assignableTypes = AdminController.class)
public class AdminExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Map<String, Object> handleException(Exception e) {
        return Map.of("success", false, "message", e.getMessage() != null ? e.getMessage() : "未知错误");
    }
} 