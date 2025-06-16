package com.fitness.booking.config;

import com.fitness.booking.dao.UserDao;
import com.fitness.booking.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserDao userDao;

    @Override
    public void run(String... args) throws Exception {
        // 检查是否已有测试用户
        if (!userDao.findByUsername("testuser").isPresent()) {
            // 创建测试用户
            User testUser = new User();
            testUser.setUsername("testuser");
            testUser.setPasswordHash("123456"); // 简单密码，生产环境应加密
            testUser.setEmail("test@example.com");
            testUser.setFullName("测试用户");
            testUser.setGender("男");
            testUser.setFitnessLevel("Beginner");
            testUser.setStatus("active");
            testUser.setUserRole("用户");
            
            userDao.save(testUser);
            System.out.println("测试用户创建成功: testuser / 123456");
        }
        
        // 检查是否已有管理员用户
        if (!userDao.findByUsername("admin").isPresent()) {
            // 创建管理员用户
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPasswordHash("admin123"); // 简单密码，生产环境应加密
            adminUser.setEmail("admin@example.com");
            adminUser.setFullName("系统管理员");
            adminUser.setGender("男");
            adminUser.setFitnessLevel("Advanced");
            adminUser.setStatus("active");
            adminUser.setUserRole("管理员");
            
            userDao.save(adminUser);
            System.out.println("管理员用户创建成功: admin / admin123");
        }
    }
} 