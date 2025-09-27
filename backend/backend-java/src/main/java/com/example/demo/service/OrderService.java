package com.example.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.auth.dto.OrderItemRequest;
import com.example.demo.entities.Order;
import com.example.demo.entities.OrderItem;
import com.example.demo.entities.User;
import com.example.demo.entities.Products;
import com.example.demo.repos.OrderRepository;
import com.example.demo.repos.ProductRepository;
import com.example.demo.repos.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    

    @Transactional
    public Order createOrder(UUID userId, List<OrderItemRequest> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));


        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setUser(user);
       for (OrderItemRequest req : items) {
    Products product = productRepository.findById(req.productId())
        .orElseThrow(() -> new RuntimeException("Product not found"));

    OrderItem item = new OrderItem();
    item.setId(UUID.randomUUID());
    item.setOrder(order);
    item.setProduct(product);
    item.setQuantity(req.quantity());
    item.setPrice(product.getPrice());

    order.getItems().add(item);
}
        return orderRepository.save(order);
    }

    public List<Order> getOrdersForUser(UUID userId) {
        return orderRepository.findByUserId(userId);
    }
}

