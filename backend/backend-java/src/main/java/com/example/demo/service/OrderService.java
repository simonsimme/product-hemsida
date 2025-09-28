package com.example.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.auth.dto.OrderItemRequest;
import com.example.demo.auth.dto.OrderRequest;
import com.example.demo.entities.Order;
import com.example.demo.entities.OrderItem;
import com.example.demo.entities.Products;
import com.example.demo.entities.User;
import com.example.demo.repos.OrderRepository;
import com.example.demo.repos.ProductRepository;
import com.example.demo.repos.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Order createOrder(OrderRequest orderRequest) {
        if (orderRequest.items() == null || orderRequest.items().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        User user = userRepository.findById(orderRequest.userId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setUser(user);

        for (OrderItemRequest req : orderRequest.items()) {
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

    public Order createOrUpdateOrder(OrderRequest orderRequest) {
        if (orderRequest.items() == null || orderRequest.items().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        User user = userRepository.findById(orderRequest.userId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Check for an existing active order
        List<Order> existingOrders = orderRepository.findByUserId(user.getId());
        Order activeOrder = existingOrders.stream()
            .filter(order -> order.getStatus().equals("ACTIVE"))
            .findFirst()
            .orElse(null);

        if (activeOrder == null) {
            // Create a new order if no active order exists
            activeOrder = new Order();
            activeOrder.setId(UUID.randomUUID());
            activeOrder.setUser(user);
        }

        for (OrderItemRequest req : orderRequest.items()) {
            Products product = productRepository.findById(req.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem item = new OrderItem();
            item.setId(UUID.randomUUID());
            item.setOrder(activeOrder);
            item.setProduct(product);
            item.setQuantity(req.quantity());
            item.setPrice(product.getPrice());

            activeOrder.getItems().add(item);
        }

        return orderRepository.save(activeOrder);
    }

    public List<Order> getOrdersForUser(UUID userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order getActiveOrder(UUID userId) {
        return orderRepository.findByUserId(userId).stream()
            .filter(order -> "ACTIVE".equals(order.getStatus()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No active order found for user"));
    }

    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }
}

