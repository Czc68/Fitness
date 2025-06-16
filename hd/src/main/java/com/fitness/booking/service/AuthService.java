package com.fitness.booking.service;

import com.fitness.booking.dao.UserDao;
import com.fitness.booking.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserDao userDao;

    // 简单的内存token存储（生产环境应使用Redis或数据库）
    private Map<String, Integer> tokenStore = new HashMap<>();

    public Map<String, Object> login(String username, String password) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Optional<User> userOpt = userDao.findByUsername(username);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                // 简单的密码验证（生产环境应使用加密）
                if (user.getPasswordHash().equals(password)) {
                    // 生成token
                    String token = UUID.randomUUID().toString();
                    tokenStore.put(token, user.getUserId());
                    
                    // 创建用户信息（不包含密码）
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("userId", user.getUserId());
                    userInfo.put("username", user.getUsername());
                    userInfo.put("email", user.getEmail());
                    userInfo.put("fullName", user.getFullName());
                    userInfo.put("gender", user.getGender());
                    userInfo.put("fitnessLevel", user.getFitnessLevel());
                    userInfo.put("userRole", user.getUserRole());
                    
                    result.put("success", true);
                    result.put("message", "登录成功");
                    result.put("token", token);
                    result.put("user", userInfo);
                } else {
                    result.put("success", false);
                    result.put("message", "密码错误");
                }
            } else {
                result.put("success", false);
                result.put("message", "用户不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "登录失败：" + e.getMessage());
        }
        
        return result;
    }

    public boolean validateToken(String token) {
        return tokenStore.containsKey(token);
    }

    public Integer getUserIdFromToken(String token) {
        return tokenStore.get(token);
    }

    public void logout(String token) {
        tokenStore.remove(token);
    }

    public Optional<User> getUserByToken(String token) {
        Integer userId = tokenStore.get(token);
        if (userId != null) {
            return userDao.findByUserId(userId);
        }
        return Optional.empty();
    }
} 