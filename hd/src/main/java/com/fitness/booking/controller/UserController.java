package com.fitness.booking.controller;

import com.fitness.booking.service.AuthService;
import com.fitness.booking.service.CheckinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private AuthService authService;

    @Autowired
    private CheckinService checkinService;

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getUserProfile(@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        
        if (token == null || !authService.validateToken(token)) {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "Token无效"
            ));
        }
        
        Integer userId = authService.getUserIdFromToken(token);
        if (userId == null) {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "用户ID无效"
            ));
        }
        
        // 获取用户统计信息
        Map<String, Object> statsResult = checkinService.getUserStats(userId);
        
        if (statsResult.get("success").equals(true)) {
            return ResponseEntity.ok(Map.of(
                "success", true,
                "stats", statsResult.get("stats")
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "获取用户信息失败"
            ));
        }
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        
        if (token == null || !authService.validateToken(token)) {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "Token无效"
            ));
        }
        
        var userOpt = authService.getUserByToken(token);
        if (userOpt.isPresent()) {
            var user = userOpt.get();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "user", Map.of(
                    "userId", user.getUserId(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "fullName", user.getFullName(),
                    "gender", user.getGender(),
                    "fitnessLevel", user.getFitnessLevel(),
                    "userRole", user.getUserRole()
                )
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "用户不存在"
            ));
        }
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
} 