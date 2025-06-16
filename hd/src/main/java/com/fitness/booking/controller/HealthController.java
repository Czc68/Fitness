package com.fitness.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 测试数据库连接
            String dbVersion = jdbcTemplate.queryForObject("SELECT @@VERSION", String.class);
            
            result.put("status", "UP");
            result.put("database", "Connected");
            result.put("databaseVersion", dbVersion);
            result.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            result.put("status", "DOWN");
            result.put("database", "Disconnected");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
        }
        
        return result;
    }

    @GetMapping("/api/test")
    public Map<String, Object> testApi() {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "API is working!");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }
} 