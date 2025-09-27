package com.example.demo.repos;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entities.Products;
public interface ProductRepository extends JpaRepository<Products, UUID> {
    //List<Products> findByOrderId(UUID orderId);
    List<Products> findByTitle(String title);

}