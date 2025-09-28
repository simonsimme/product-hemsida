package com.example.demo.repos;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    List<OrderItem> findByOrderId(UUID orderId);
    List<OrderItem> findAllByOrderId(UUID orderId);
}

