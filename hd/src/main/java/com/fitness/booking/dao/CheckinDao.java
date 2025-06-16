package com.fitness.booking.dao;

import com.fitness.booking.model.Checkin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class CheckinDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Checkin> checkinRowMapper = new RowMapper<Checkin>() {
        @Override
        public Checkin mapRow(ResultSet rs, int rowNum) throws SQLException {
            Checkin checkin = new Checkin();
            checkin.setCheckinId(rs.getInt("checkin_id"));
            checkin.setUserId(rs.getInt("user_id"));
            checkin.setCheckinDate(rs.getDate("checkin_date").toLocalDate());
            checkin.setWeight(rs.getDouble("weight"));
            checkin.setSleepHours(rs.getDouble("sleep_hours"));
            checkin.setWaterCups(rs.getInt("water_cups"));
            checkin.setSteps(rs.getInt("steps"));
            checkin.setMood(rs.getString("mood"));
            checkin.setCompletedPlanToday(rs.getBoolean("completed_plan_today"));
            checkin.setGeneralNotes(rs.getString("general_notes"));
            checkin.setCreatedAt(rs.getTimestamp("created_at") != null ? 
                rs.getTimestamp("created_at").toLocalDateTime() : null);
            return checkin;
        }
    };

    public List<Checkin> findByUserId(Integer userId) {
        String sql = "SELECT * FROM checkins WHERE user_id = ? ORDER BY checkin_date DESC";
        return jdbcTemplate.query(sql, checkinRowMapper, userId);
    }

    public Optional<Checkin> findByUserIdAndDate(Integer userId, LocalDate date) {
        String sql = "SELECT * FROM checkins WHERE user_id = ? AND checkin_date = ?";
        List<Checkin> checkins = jdbcTemplate.query(sql, checkinRowMapper, userId, date);
        return checkins.isEmpty() ? Optional.empty() : Optional.of(checkins.get(0));
    }

    public Optional<Checkin> findById(Integer checkinId) {
        String sql = "SELECT * FROM checkins WHERE checkin_id = ?";
        List<Checkin> checkins = jdbcTemplate.query(sql, checkinRowMapper, checkinId);
        return checkins.isEmpty() ? Optional.empty() : Optional.of(checkins.get(0));
    }

    public Checkin save(Checkin checkin) {
        if (checkin.getCheckinId() == null) {
            // 插入新打卡记录
            String sql = "INSERT INTO checkins (user_id, checkin_date, weight, sleep_hours, water_cups, " +
                        "steps, mood, completed_plan_today, general_notes) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            jdbcTemplate.update(sql,
                checkin.getUserId(),
                checkin.getCheckinDate(),
                checkin.getWeight(),
                checkin.getSleepHours(),
                checkin.getWaterCups(),
                checkin.getSteps(),
                checkin.getMood(),
                checkin.getCompletedPlanToday(),
                checkin.getGeneralNotes()
            );
            
            // 获取生成的ID
            String idSql = "SELECT SCOPE_IDENTITY()";
            Integer checkinId = jdbcTemplate.queryForObject(idSql, Integer.class);
            checkin.setCheckinId(checkinId);
        } else {
            // 更新现有打卡记录
            String sql = "UPDATE checkins SET weight = ?, sleep_hours = ?, water_cups = ?, " +
                        "steps = ?, mood = ?, completed_plan_today = ?, general_notes = ? " +
                        "WHERE checkin_id = ?";
            
            jdbcTemplate.update(sql,
                checkin.getWeight(),
                checkin.getSleepHours(),
                checkin.getWaterCups(),
                checkin.getSteps(),
                checkin.getMood(),
                checkin.getCompletedPlanToday(),
                checkin.getGeneralNotes(),
                checkin.getCheckinId()
            );
        }
        
        return checkin;
    }

    public boolean delete(Integer checkinId) {
        String sql = "DELETE FROM checkins WHERE checkin_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, checkinId);
        return rowsAffected > 0;
    }

    public int countByUserId(Integer userId) {
        String sql = "SELECT COUNT(*) FROM checkins WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }

    public int getCurrentStreak(Integer userId) {
        String sql = "WITH RankedCheckins AS (" +
                    "  SELECT checkin_date, ROW_NUMBER() OVER (ORDER BY checkin_date DESC) as rn " +
                    "  FROM checkins WHERE user_id = ?" +
                    ") " +
                    "SELECT COUNT(*) FROM RankedCheckins r1 " +
                    "WHERE NOT EXISTS (" +
                    "  SELECT 1 FROM RankedCheckins r2 " +
                    "  WHERE r2.rn = r1.rn + 1 " +
                    "  AND DATEDIFF(day, r2.checkin_date, r1.checkin_date) = 1" +
                    ")";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }

    public Double getAverageWeight(Integer userId) {
        String sql = "SELECT AVG(weight) FROM checkins WHERE user_id = ? AND weight IS NOT NULL";
        return jdbcTemplate.queryForObject(sql, Double.class, userId);
    }

    public Double getAverageSleep(Integer userId) {
        String sql = "SELECT AVG(sleep_hours) FROM checkins WHERE user_id = ? AND sleep_hours IS NOT NULL";
        return jdbcTemplate.queryForObject(sql, Double.class, userId);
    }

    public List<Checkin> findRecentByUserId(Integer userId, int limit) {
        String sql = "SELECT TOP (?) * FROM checkins WHERE user_id = ? ORDER BY checkin_date DESC";
        return jdbcTemplate.query(sql, checkinRowMapper, limit, userId);
    }
} 