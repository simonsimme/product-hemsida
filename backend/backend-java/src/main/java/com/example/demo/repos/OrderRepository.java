package com.example.demo.repos;
import com.example.demo.entities.Order;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUserId(UUID userId);

    @Query("SELECT o FROM Order o JOIN FETCH o.user WHERE o.user.id = :userId")
    List<Order> findOrdersWithUser(@Param("userId") UUID userId);
}

