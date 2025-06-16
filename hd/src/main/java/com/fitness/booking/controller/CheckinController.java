package com.fitness.booking.controller;

import com.fitness.booking.service.AuthService;
import com.fitness.booking.service.CheckinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/checkins")
@CrossOrigin(origins = "*")
public class CheckinController {

    @Autowired
    private AuthService authService;

    @Autowired
    private CheckinService checkinService;

    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitCheckin(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> checkinData) {
        
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
        
        Map<String, Object> result = checkinService.submitCheckin(userId, checkinData);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> getCheckinHistory(@RequestHeader("Authorization") String authHeader) {
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
        
        Map<String, Object> result = checkinService.getCheckinHistory(userId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{checkinId}")
    public ResponseEntity<Map<String, Object>> deleteCheckin(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer checkinId) {
        
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
        
        Map<String, Object> result = checkinService.deleteCheckin(checkinId, userId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{checkinId}")
    public ResponseEntity<Map<String, Object>> updateCheckin(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer checkinId,
            @RequestBody Map<String, Object> checkinData) {
        
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
        
        Map<String, Object> result = checkinService.updateCheckin(checkinId, userId, checkinData);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getUserStats(@RequestHeader("Authorization") String authHeader) {
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
        
        Map<String, Object> result = checkinService.getUserStats(userId);
        return ResponseEntity.ok(result);
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
} 