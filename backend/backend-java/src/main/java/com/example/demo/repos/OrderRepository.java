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

import com.example.demo.entities.Order;
import com.example.demo.entities.User;

@Repository
public class OrderRepository {
    
    private final DataSource dataSource;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    
    public OrderRepository(DataSource dataSource, UserRepository userRepository, OrderItemRepository orderItemRepository) {
        this.dataSource = dataSource;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
    }
    
    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(UUID.fromString(rs.getString("id")));
        order.setStatus(rs.getString("status"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            order.setCreatedAt(createdAt.toLocalDateTime().atOffset(OffsetDateTime.now().getOffset()));
        }
        
        return order;
    }
    
    public List<Order> findByUserId(UUID userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ?";
        List<Order> orders = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setObject(1, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while finding orders by user ID", e);
        }
        
        
        for (Order order : orders) {
            populateOrderDetails(order, userId);
        }
        
        return orders;
    }
    
    public List<Order> findOrdersWithUser(UUID userId) {
        return findByUserId(userId);
    }
    
    public Optional<Order> findById(UUID id) {
        String sql = "SELECT o.*, o.user_id FROM orders o WHERE o.id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setObject(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    UUID userId = UUID.fromString(rs.getString("user_id"));
                    populateOrderDetails(order, userId);
                    return Optional.of(order);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while finding order by ID", e);
        }
    }
    // laddar user och orderitems, Sätter order.user
    private void populateOrderDetails(Order order, UUID userId) {
        //laddar in user till order.user
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(order::setUser);

        //laddar in order.items till ordern

        order.setItems(orderItemRepository.findByOrderId(order.getId()));
    }
    
    public Order save(Order order) {
        if (findById(order.getId()).isPresent()) {
            String sql = "UPDATE orders SET user_id = ?, status = ?, created_at = ? WHERE id = ?";
            
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                
                ps.setObject(1, order.getUser().getId());
                ps.setString(2, order.getStatus());
                ps.setTimestamp(3, order.getCreatedAt() != null ? Timestamp.valueOf(order.getCreatedAt().toLocalDateTime()) : null);
                ps.setObject(4, order.getId());
                
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Database error while updating order", e);
            }
        } else {
            String sql = "INSERT INTO orders (id, user_id, status, created_at) VALUES (?, ?, ?, ?)";
            
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                
                ps.setObject(1, order.getId());
                ps.setObject(2, order.getUser().getId());
                ps.setString(3, order.getStatus());
                ps.setTimestamp(4, order.getCreatedAt() != null ? Timestamp.valueOf(order.getCreatedAt().toLocalDateTime()) : null);
                
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Database error while inserting order", e);
            }
        }
        
            
        if (order.getItems() != null) { // om ordern har items i sig
            for (var item : order.getItems()) {
                item.setOrder(order); //sätt order referensen på item
                orderItemRepository.save(item);
            }
        }
        
        return order;
    }
    
    public void delete(Order order) {
        deleteById(order.getId());
    }
    
    public void deleteById(UUID id) {
        orderItemRepository.deleteByOrderId(id);
        String sql = "DELETE FROM orders WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Database error while deleting order by ID", e);
        }
    }

    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM orders WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setObject(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while checking order existence", e);
        }
    }

    public void deleteAll() {
        String sql = "DELETE FROM orders";
        
        try (Connection conn = dataSource.getConnection();
             Statement ps = conn.createStatement()) {
            
            ps.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Database error while deleting all orders", e);
        }
    }
}

