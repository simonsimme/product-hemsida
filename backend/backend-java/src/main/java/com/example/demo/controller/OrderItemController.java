package com.example.demo.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.auth.dto.OrderItemRequest;
import com.example.demo.auth.dto.OrderRequest;
import com.example.demo.entities.Order;
import com.example.demo.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order-items")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class OrderItemController {

    private final OrderService orderService;

    // Create a new order for a user
    @PostMapping("/create/{userId}")
    
    public ResponseEntity<Order> createOrder(
            @PathVariable UUID userId,
            @RequestBody List<OrderItemRequest> items
    ) {
        OrderRequest orderRequest = new OrderRequest(userId, items);
        Order order = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersForUser(@PathVariable UUID userId) {
        List<Order> orders = orderService.getOrdersForUser(userId);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/create-or-update/{userId}")
    public ResponseEntity<Order> createOrUpdateOrder(
            @PathVariable UUID userId,
            @RequestBody Object items
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<OrderItemRequest> itemList;

        if (items instanceof List<?> itemListRaw) {
            itemList = itemListRaw.stream()
                .map(item -> objectMapper.convertValue(item, OrderItemRequest.class))
                .toList();
        } else {
            itemList = List.of(objectMapper.convertValue(items, OrderItemRequest.class));
        }

        OrderRequest orderRequest = new OrderRequest(userId, itemList);
        Order order = orderService.createOrUpdateOrder(orderRequest);
        return ResponseEntity.ok(order);
    }
}

