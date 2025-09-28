package com.example.demo.repos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.example.demo.entities.User;

@Repository
public class UserRepository {
    
    private final DataSource dataSource;
    
    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    //ger en user från en resultset
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(UUID.fromString(rs.getString("id")));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setRole(rs.getString("role"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime().atOffset(OffsetDateTime.now().getOffset()));
        }
        
        return user;
    }
    
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = mapResultSetToUser(rs);
                    return Optional.of(user);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while finding user by email", e);
        }
    }
    
    public Optional<User> findById(UUID userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setObject(1, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = mapResultSetToUser(rs);
                    return Optional.of(user);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while finding user by ID", e);
        }
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while checking email existence", e);
        }
    }

    public User save(User user) {
        if (findById(user.getId()).isPresent()) {
            
            String sql = "UPDATE users SET email = ?, password_hash = ?, role = ?, created_at = ? WHERE id = ?";
            
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                
                ps.setString(1, user.getEmail());
                ps.setString(2, user.getPasswordHash());
                ps.setString(3, user.getRole());
                ps.setTimestamp(4, user.getCreatedAt() != null ? Timestamp.valueOf(user.getCreatedAt().toLocalDateTime()) : null);
                ps.setObject(5, user.getId());
                
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Database error while updating user", e);
            }
        } else {
            String sql = "INSERT INTO users (id, email, password_hash, role, created_at) VALUES (?, ?, ?, ?, ?)";
            
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                
                ps.setObject(1, user.getId());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getPasswordHash());
                ps.setString(4, user.getRole());
                ps.setTimestamp(5, user.getCreatedAt() != null ? Timestamp.valueOf(user.getCreatedAt().toLocalDateTime()) : null);
                
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Database error while inserting user", e);
            }
        }
        return user;
    }

    public void delete(User user) {
        deleteById(user.getId());
    }

    public void deleteById(UUID id) {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Database error while deleting user by ID", e);
        }
    }

    public void deleteAll() {
        String sql = "DELETE FROM users";
        
        try (Connection conn = dataSource.getConnection();
             Statement ps = conn.createStatement()) {
            
            ps.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Database error while deleting all users", e);
        }
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             Statement ps = conn.createStatement();
             ResultSet rs = ps.executeQuery(sql)) {
            
            while (rs.next()) {
                User user = mapResultSetToUser(rs);
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while finding all users", e);
        }
        
        return users;
    }
}