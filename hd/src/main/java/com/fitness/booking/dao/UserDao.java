package com.fitness.booking.dao;

import com.fitness.booking.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(rs.getInt("user_id"));
            user.setUsername(rs.getString("username"));
            user.setPasswordHash(rs.getString("password_hash"));
            user.setEmail(rs.getString("email"));
            user.setFullName(rs.getString("full_name"));
            user.setDateOfBirth(rs.getDate("date_of_birth") != null ? 
                rs.getDate("date_of_birth").toLocalDate() : null);
            user.setGender(rs.getString("gender"));
            user.setHeight(rs.getDouble("height"));
            user.setWeight(rs.getDouble("weight"));
            user.setFitnessLevel(rs.getString("fitness_level"));
            user.setProfilePictureUrl(rs.getString("profile_picture_url"));
            user.setCreatedAt(rs.getTimestamp("created_at") != null ? 
                rs.getTimestamp("created_at").toLocalDateTime() : null);
            user.setUpdatedAt(rs.getTimestamp("updated_at") != null ? 
                rs.getTimestamp("updated_at").toLocalDateTime() : null);
            user.setStatus(rs.getString("status"));
            user.setUserRole(rs.getString("user_role"));
            return user;
        }
    };

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ? AND status = 'active'";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, username);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    public Optional<User> findByUserId(Integer userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, userId);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    public boolean validatePassword(String username, String passwordHash) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password_hash = ? AND status = 'active'";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, username, passwordHash);
        return count > 0;
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users WHERE status = 'active' ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    public User save(User user) {
        if (user.getUserId() == null) {
            // 插入新用户
            String sql = "INSERT INTO users (username, password_hash, email, full_name, date_of_birth, " +
                        "gender, height, weight, fitness_level, profile_picture_url, status, user_role) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            jdbcTemplate.update(sql,
                user.getUsername(),
                user.getPasswordHash(),
                user.getEmail(),
                user.getFullName(),
                user.getDateOfBirth(),
                user.getGender(),
                user.getHeight(),
                user.getWeight(),
                user.getFitnessLevel(),
                user.getProfilePictureUrl(),
                user.getStatus(),
                user.getUserRole()
            );
            
            // 获取生成的ID
            String idSql = "SELECT SCOPE_IDENTITY()";
            Integer userId = jdbcTemplate.queryForObject(idSql, Integer.class);
            user.setUserId(userId);
        } else {
            // 更新现有用户
            String sql = "UPDATE users SET username = ?, email = ?, full_name = ?, date_of_birth = ?, " +
                        "gender = ?, height = ?, weight = ?, fitness_level = ?, profile_picture_url = ?, " +
                        "status = ?, user_role = ? WHERE user_id = ?";
            
            jdbcTemplate.update(sql,
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getDateOfBirth(),
                user.getGender(),
                user.getHeight(),
                user.getWeight(),
                user.getFitnessLevel(),
                user.getProfilePictureUrl(),
                user.getStatus(),
                user.getUserRole(),
                user.getUserId()
            );
        }
        
        return user;
    }

    public boolean delete(Integer userId) {
        String sql = "UPDATE users SET status = 'inactive' WHERE user_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, userId);
        return rowsAffected > 0;
    }
} 