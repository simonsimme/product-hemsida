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

import com.example.demo.entities.Products;

@Repository
public class ProductRepository {
    
    private final DataSource dataSource;
    
    public ProductRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    private Products mapResultSetToProduct(ResultSet rs) throws SQLException {
        Products product = new Products();
        product.setId(UUID.fromString(rs.getString("id")));
        product.setTitle(rs.getString("title"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getDouble("price"));
        product.setQuantity(rs.getInt("quantity"));
        product.setImageUrl(rs.getString("image_url"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            product.setCreatedAt(createdAt.toLocalDateTime().atOffset(OffsetDateTime.now().getOffset()));
        }
        
        return product;
    }
    
    public List<Products> findAll() {
        String sql = "SELECT * FROM products";
        List<Products> products = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             Statement ps = conn.createStatement();
             ResultSet rs = ps.executeQuery(sql)) {
            
            while (rs.next()) {
                Products product = mapResultSetToProduct(rs);
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while finding all products", e);
        }
        
        return products;
    }
    
    public Optional<Products> findById(UUID id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setObject(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Products product = mapResultSetToProduct(rs);
                    return Optional.of(product);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while finding product by ID", e);
        }
    }
    //ILIKE postgres syntax för att söka ovavät caps eller ej
    public List<Products> findByTitle(String title) {
        String sql = "SELECT * FROM products WHERE title ILIKE ?";
        List<Products> products = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, "%" + title + "%");//wildcards för att hitta de relevanta
            
            try (ResultSet rs = ps.executeQuery()) { // ger resultatSET
                while (rs.next()) {
                    Products product = mapResultSetToProduct(rs);
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while finding products by title", e);
        }
        
        return products;
    }
    
    public Products save(Products product) {
        if (findById(product.getId()).isPresent()) {
            
            String sql = "UPDATE products SET title = ?, description = ?, price = ?, quantity = ?, image_url = ?, created_at = ? WHERE id = ?";
            
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                
                ps.setString(1, product.getTitle());
                ps.setString(2, product.getDescription());
                ps.setDouble(3, product.getPrice());
                ps.setInt(4, product.getQuantity());
                ps.setString(5, product.getImageUrl());
                ps.setTimestamp(6, product.getCreatedAt() != null ? Timestamp.valueOf(product.getCreatedAt().toLocalDateTime()) : null);
                ps.setObject(7, product.getId());
                
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Database error while updating product", e);
            }
        } else {
            
            String sql = "INSERT INTO products (id, title, description, price, quantity, image_url, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                
                ps.setObject(1, product.getId());
                ps.setString(2, product.getTitle());
                ps.setString(3, product.getDescription());
                ps.setDouble(4, product.getPrice());
                ps.setInt(5, product.getQuantity());
                ps.setString(6, product.getImageUrl());
                ps.setTimestamp(7, product.getCreatedAt() != null ? Timestamp.valueOf(product.getCreatedAt().toLocalDateTime()) : null);
                
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Database error while inserting product", e);
            }
        }
        return product;
    }
    
    public void delete(Products product) {
        deleteById(product.getId());
    }
    
    public void deleteById(UUID id) {
        String sql = "DELETE FROM products WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Database error while deleting product by ID", e);
        }
    }

    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM products WHERE id = ?";
        
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
            throw new RuntimeException("Database error while checking product existence", e);
        }
    }

    public void deleteAll() {
        String sql = "DELETE FROM products";
        
        try (Connection conn = dataSource.getConnection();
             Statement ps = conn.createStatement()) {
            
            ps.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Database error while deleting all products", e);
        }
    }
}