package com.fitness.booking.controller;

import com.fitness.booking.dao.UserDao;
import com.fitness.booking.dao.CheckinDao;
import com.fitness.booking.model.User;
import com.fitness.booking.model.Checkin;
import com.fitness.booking.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@RestControllerAdvice(assignableTypes = AdminController.class)
public class AdminController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private CheckinDao checkinDao;
    @Autowired
    private AuthService authService;

    // 管理员权限校验
    private boolean isAdmin(String authHeader) {
        String token = extractToken(authHeader);
        return token != null && authService.validateToken(token) &&
                "管理员".equals(authService.getUserRoleFromToken(token));
    }
    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    // 获取所有用户
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String authHeader) {
        if (!isAdmin(authHeader)) return ResponseEntity.status(403).body(Map.of("success", false, "message", "无权限"));
        List<User> users = userDao.findAll();
        return ResponseEntity.ok(Map.of("success", true, "users", users));
    }

    // 获取单个用户信息
    @GetMapping("/user/detail/{id}")
    public ResponseEntity<?> getUser(@PathVariable Integer id, @RequestHeader("Authorization") String authHeader) {
        if (!isAdmin(authHeader)) return ResponseEntity.status(403).body(Map.of("success", false, "message", "无权限"));
        return userDao.findByUserId(id)
                .map(user -> ResponseEntity.ok(Map.of("success", true, "user", user)))
                .orElse(ResponseEntity.ok(Map.of("success", false, "message", "用户不存在，userId=" + id)));
    }

    // 修改用户信息/权限
    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody User user, @RequestHeader("Authorization") String authHeader) {
        if (!isAdmin(authHeader)) return ResponseEntity.status(403).body(Map.of("success", false, "message", "无权限"));
        user.setUserId(id);
        userDao.save(user);
        return ResponseEntity.ok(Map.of("success", true));
    }

    // 删除用户
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id, @RequestHeader("Authorization") String authHeader) {
        if (!isAdmin(authHeader)) return ResponseEntity.status(403).body(Map.of("success", false, "message", "无权限"));
        boolean ok = userDao.delete(id);
        return ResponseEntity.ok(Map.of("success", ok));
    }

    // 获取所有打卡数据
    @GetMapping("/checkins")
    public ResponseEntity<?> getAllCheckins(@RequestHeader("Authorization") String authHeader) {
        if (!isAdmin(authHeader)) return ResponseEntity.status(403).body(Map.of("success", false, "message", "无权限"));
        // 简单实现：查所有用户的打卡
        // 你可以扩展为分页、筛选等
        List<Checkin> all = new java.util.ArrayList<>();
        for (User u : userDao.findAll()) {
            all.addAll(checkinDao.findByUserId(u.getUserId()));
        }
        return ResponseEntity.ok(Map.of("success", true, "checkins", all));
    }

    // 修改打卡数据
    @PutMapping("/checkin/{id}")
    public ResponseEntity<?> updateCheckin(@PathVariable Integer id, @RequestBody Checkin checkin, @RequestHeader("Authorization") String authHeader) {
        if (!isAdmin(authHeader)) return ResponseEntity.status(403).body(Map.of("success", false, "message", "无权限"));
        checkin.setCheckinId(id);
        checkinDao.save(checkin);
        return ResponseEntity.ok(Map.of("success", true));
    }

    // 删除打卡数据
    @DeleteMapping("/checkin/{id}")
    public ResponseEntity<?> deleteCheckin(@PathVariable Integer id, @RequestHeader("Authorization") String authHeader) {
        if (!isAdmin(authHeader)) return ResponseEntity.status(403).body(Map.of("success", false, "message", "无权限"));
        boolean ok = checkinDao.delete(id);
        return ResponseEntity.ok(Map.of("success", ok));
    }
} 