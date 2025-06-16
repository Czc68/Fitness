package com.fitness.booking.controller;

import com.fitness.booking.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "用户名和密码不能为空"
            ));
        }
        
        Map<String, Object> result = authService.login(username, password);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        if (token != null) {
            authService.logout(token);
        }
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "退出登录成功"
        ));
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        
        if (token == null || !authService.validateToken(token)) {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "Token无效"
            ));
        }
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Token有效"
        ));
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
} 