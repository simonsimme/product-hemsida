package com.example.demo.repos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.example.demo.entities.OrderItem;
import com.example.demo.entities.Products;

@Repository
public class OrderItemRepository {
    
    private final DataSource dataSource;
    private final ProductRepository productRepository;
    
    public OrderItemRepository(DataSource dataSource, ProductRepository productRepository) {
        this.dataSource = dataSource;
        this.productRepository = productRepository;
    }
    
    private OrderItem mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(UUID.fromString(rs.getString("id")));
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setPrice(rs.getDouble("price"));
        return orderItem;
    }
    
    public List<OrderItem> findByOrderId(UUID orderId) {
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        List<OrderItem> orderItems = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setObject(1, orderId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem orderItem = mapResultSetToOrderItem(rs);
                    orderItems.add(orderItem);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while finding order items by order ID", e);
        }
        
        for (OrderItem item : orderItems) {
            UUID productId = getProductIdForOrderItem(item.getId()); // hämtar product_id
            Optional<Products> product = productRepository.findById(productId); // sätter product id till på itemorder
            product.ifPresent(item::setProduct);
        }
        
        return orderItems;
    }
    
    public List<OrderItem> findAllByOrderId(UUID orderId) {
        return findByOrderId(orderId);
    }
    
    private UUID getProductIdForOrderItem(UUID orderItemId) {
        String sql = "SELECT product_id FROM order_items WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setObject(1, orderItemId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String productIdStr = rs.getString("product_id");
                    return UUID.fromString(productIdStr);
                } else {
                    throw new RuntimeException("Order item not found: " + orderItemId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while getting product ID", e);
        }
    }
    
    public Optional<OrderItem> findById(UUID id) {
        String sql = "SELECT * FROM order_items WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setObject(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    OrderItem orderItem = mapResultSetToOrderItem(rs);
                    
                    UUID productId = getProductIdForOrderItem(orderItem.getId());
                    Optional<Products> product = productRepository.findById(productId);
                    product.ifPresent(orderItem::setProduct); //koppla orderitem till product
                    
                    return Optional.of(orderItem);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while finding order item by ID", e);
        }
    }
    
    public OrderItem save(OrderItem orderItem) {
        if (findById(orderItem.getId()).isPresent()) {
            String sql = "UPDATE order_items SET order_id = ?, product_id = ?, quantity = ?, price = ? WHERE id = ?";
            
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                
                ps.setObject(1, orderItem.getOrder().getId());
                ps.setObject(2, orderItem.getProduct().getId());
                ps.setInt(3, orderItem.getQuantity());
                ps.setDouble(4, orderItem.getPrice());
                ps.setObject(5, orderItem.getId());
                
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Database error while updating order item", e);
            }
        } else {
            String sql = "INSERT INTO order_items (id, order_id, product_id, quantity, price) VALUES (?, ?, ?, ?, ?)";
            
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                
                ps.setObject(1, orderItem.getId());
                ps.setObject(2, orderItem.getOrder().getId());
                ps.setObject(3, orderItem.getProduct().getId());
                ps.setInt(4, orderItem.getQuantity());
                ps.setDouble(5, orderItem.getPrice());
                
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Database error while inserting order item", e);
            }
        }
        return orderItem;
    }
    
    public void delete(OrderItem orderItem) {
        deleteById(orderItem.getId());
    }
    
    public void deleteById(UUID id) {
        String sql = "DELETE FROM order_items WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Database error while deleting order item by ID", e);
        }
    }

    public void deleteByOrderId(UUID orderId) {
        String sql = "DELETE FROM order_items WHERE order_id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setObject(1, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Database error while deleting order items by order ID", e);
        }
    }

    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM order_items WHERE id = ?";
        
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
            throw new RuntimeException("Database error while checking order item existence", e);
        }
    }

    public void deleteAll() {
        String sql = "DELETE FROM order_items";
        
        try (Connection conn = dataSource.getConnection();
             Statement ps = conn.createStatement()) {
            
            ps.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Database error while deleting all order items", e);
        }
    }
}

