package com.fitness.booking.service;

import com.fitness.booking.dao.CheckinDao;
import com.fitness.booking.model.Checkin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CheckinService {

    @Autowired
    private CheckinDao checkinDao;

    public Map<String, Object> submitCheckin(Integer userId, Map<String, Object> checkinData) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            LocalDate today = LocalDate.now();
            
            // 检查今天是否已经打卡
            Optional<Checkin> existingCheckin = checkinDao.findByUserIdAndDate(userId, today);
            if (existingCheckin.isPresent()) {
                result.put("success", false);
                result.put("message", "今天已经打卡了");
                return result;
            }
            
            // 创建新的打卡记录
            Checkin checkin = new Checkin(userId, today);
            
            // 设置打卡数据
            if (checkinData.get("weight") != null && !checkinData.get("weight").toString().isEmpty()) {
                checkin.setWeight(Double.parseDouble(checkinData.get("weight").toString()));
            }
            
            if (checkinData.get("sleep_hours") != null && !checkinData.get("sleep_hours").toString().isEmpty()) {
                checkin.setSleepHours(Double.parseDouble(checkinData.get("sleep_hours").toString()));
            }
            
            if (checkinData.get("water_cups") != null && !checkinData.get("water_cups").toString().isEmpty()) {
                checkin.setWaterCups(Integer.parseInt(checkinData.get("water_cups").toString()));
            }
            
            if (checkinData.get("steps") != null && !checkinData.get("steps").toString().isEmpty()) {
                checkin.setSteps(Integer.parseInt(checkinData.get("steps").toString()));
            }
            
            if (checkinData.get("mood") != null && !checkinData.get("mood").toString().isEmpty()) {
                checkin.setMood(checkinData.get("mood").toString());
            }
            
            if (checkinData.get("completed_plan_today") != null) {
                checkin.setCompletedPlanToday(Boolean.parseBoolean(checkinData.get("completed_plan_today").toString()));
            }
            
            if (checkinData.get("general_notes") != null) {
                checkin.setGeneralNotes(checkinData.get("general_notes").toString());
            }
            
            // 保存打卡记录
            Checkin savedCheckin = checkinDao.save(checkin);
            
            result.put("success", true);
            result.put("message", "打卡成功");
            result.put("checkin", savedCheckin);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "打卡失败：" + e.getMessage());
        }
        
        return result;
    }

    public Map<String, Object> getCheckinHistory(Integer userId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<Checkin> checkins = checkinDao.findByUserId(userId);
            result.put("success", true);
            result.put("checkins", checkins);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取打卡历史失败：" + e.getMessage());
        }
        
        return result;
    }

    public Map<String, Object> deleteCheckin(Integer checkinId, Integer userId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 验证打卡记录是否属于该用户
            Optional<Checkin> checkinOpt = checkinDao.findById(checkinId);
            if (checkinOpt.isPresent()) {
                Checkin checkin = checkinOpt.get();
                if (!checkin.getUserId().equals(userId)) {
                    result.put("success", false);
                    result.put("message", "无权删除此打卡记录");
                    return result;
                }
                
                boolean deleted = checkinDao.delete(checkinId);
                if (deleted) {
                    result.put("success", true);
                    result.put("message", "删除成功");
                } else {
                    result.put("success", false);
                    result.put("message", "删除失败");
                }
            } else {
                result.put("success", false);
                result.put("message", "打卡记录不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        
        return result;
    }

    public Map<String, Object> getUserStats(Integer userId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // 总打卡天数
            int totalCheckins = checkinDao.countByUserId(userId);
            stats.put("totalCheckins", totalCheckins);
            
            // 连续打卡天数
            int currentStreak = checkinDao.getCurrentStreak(userId);
            stats.put("currentStreak", currentStreak);
            
            // 平均体重
            Double avgWeight = checkinDao.getAverageWeight(userId);
            stats.put("avgWeight", avgWeight);
            
            // 平均睡眠
            Double avgSleep = checkinDao.getAverageSleep(userId);
            stats.put("avgSleep", avgSleep);
            
            result.put("success", true);
            result.put("stats", stats);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取统计数据失败：" + e.getMessage());
        }
        
        return result;
    }

    public Map<String, Object> updateCheckin(Integer checkinId, Integer userId, Map<String, Object> checkinData) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 验证打卡记录是否属于该用户
            Optional<Checkin> checkinOpt = checkinDao.findById(checkinId);
            if (checkinOpt.isPresent()) {
                Checkin checkin = checkinOpt.get();
                if (!checkin.getUserId().equals(userId)) {
                    result.put("success", false);
                    result.put("message", "无权修改此打卡记录");
                    return result;
                }
                
                // 更新打卡数据
                if (checkinData.get("weight") != null && !checkinData.get("weight").toString().isEmpty()) {
                    checkin.setWeight(Double.parseDouble(checkinData.get("weight").toString()));
                }
                
                if (checkinData.get("sleep_hours") != null && !checkinData.get("sleep_hours").toString().isEmpty()) {
                    checkin.setSleepHours(Double.parseDouble(checkinData.get("sleep_hours").toString()));
                }
                
                if (checkinData.get("water_cups") != null && !checkinData.get("water_cups").toString().isEmpty()) {
                    checkin.setWaterCups(Integer.parseInt(checkinData.get("water_cups").toString()));
                }
                
                if (checkinData.get("steps") != null && !checkinData.get("steps").toString().isEmpty()) {
                    checkin.setSteps(Integer.parseInt(checkinData.get("steps").toString()));
                }
                
                if (checkinData.get("mood") != null && !checkinData.get("mood").toString().isEmpty()) {
                    checkin.setMood(checkinData.get("mood").toString());
                }
                
                if (checkinData.get("completed_plan_today") != null) {
                    checkin.setCompletedPlanToday(Boolean.parseBoolean(checkinData.get("completed_plan_today").toString()));
                }
                
                if (checkinData.get("general_notes") != null) {
                    checkin.setGeneralNotes(checkinData.get("general_notes").toString());
                }
                
                // 保存更新
                Checkin updatedCheckin = checkinDao.save(checkin);
                
                result.put("success", true);
                result.put("message", "更新成功");
                result.put("checkin", updatedCheckin);
                
            } else {
                result.put("success", false);
                result.put("message", "打卡记录不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }
        
        return result;
    }
} 