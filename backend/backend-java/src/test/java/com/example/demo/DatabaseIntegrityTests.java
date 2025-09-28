package com.example.demo;

import com.example.demo.auth.AuthController;
import com.example.demo.auth.dto.RegisterRequest;
import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.entities.User;
import com.example.demo.entities.Order;
import com.example.demo.entities.OrderItem;
import com.example.demo.repos.OrderRepository;
import com.example.demo.repos.ProductRepository;
import com.example.demo.repos.OrderItemRepository;
import com.example.demo.repos.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DatabaseIntegrityTests {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AuthController authController;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void cleanUp() {
        orderRepository.deleteAll();
        orderItemRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testUniqueEmailConstraint() {
        RegisterRequest req1 = new RegisterRequest("unique1@example.com", "password");
        authController.register(req1);

        RegisterRequest req2 = new RegisterRequest("unique1@example.com", "password"); 

        assertThrows(IllegalArgumentException.class, () -> authController.register(req2));
    }

   

    @Test
    void testForeignKeyConstraint() {
        RegisterRequest req = new RegisterRequest("foreignkey2@example.com", "password");
        AuthResponse response = authController.register(req);

        assertThrows(DataIntegrityViolationException.class, () -> {
            Order invalidOrder = new Order();
            invalidOrder.setId(UUID.randomUUID());
            invalidOrder.setUser(null); 
            orderRepository.save(invalidOrder);
        });
    }

    @Test
    void testCascadeDelete() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("cascade@example.com");
        user.setPasswordHash("password");
        user.setRole("USER");
        userRepository.save(user);

        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setUser(user);
        orderRepository.save(order);

        OrderItem item = new OrderItem();
        item.setId(UUID.randomUUID());
        item.setOrder(order);
        item.setProduct(null);
        item.setQuantity(1);
        item.setPrice(100.0);
        order.getItems().add(item);
        orderRepository.save(order);

        orderRepository.delete(order);

        
        assertTrue(orderItemRepository.findAllByOrderId(order.getId()).isEmpty());
    }

    @Test
    void testSQLInjection() {
        String sqlInjectionPayload = "' OR '1'='1";
        RegisterRequest maliciousRequest = new RegisterRequest(sqlInjectionPayload, "password");

        assertThrows(IllegalArgumentException.class, () -> authController.register(maliciousRequest));

        assertTrue(userRepository.findAll().stream()
                .noneMatch(user -> user.getEmail().equals(sqlInjectionPayload)));
    }
}
