package com.example.demo.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.auth.dto.OrderItemRequest;
import com.example.demo.entities.Order;
import com.example.demo.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderService orderService;

    // Create a new order for a user
    @PostMapping("/create/{userId}")
    public ResponseEntity<Order> createOrder(
            @PathVariable UUID userId,
            @RequestBody List<OrderItemRequest> items
    ) {
        Order order = orderService.createOrder(userId, items);
        return ResponseEntity.ok(order);
    }

    // Get all orders for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersForUser(@PathVariable UUID userId) {
        List<Order> orders = orderService.getOrdersForUser(userId);
        return ResponseEntity.ok(orders);
    }
}

