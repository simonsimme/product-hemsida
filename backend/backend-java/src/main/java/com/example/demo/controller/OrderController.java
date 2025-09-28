package com.example.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.auth.dto.CreateOrderRequest;
import com.example.demo.entities.Order;
import com.example.demo.service.OrderService;
import com.example.demo.auth.dto.OrderRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) {
        // Fetch the active order for the user
        Order activeOrder = orderService.getActiveOrder(orderRequest.userId());

        // Update the status to SENT
        activeOrder.setStatus("SENT");

        // Save the updated order
        Order updatedOrder = orderService.updateOrder(activeOrder);

        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrders(@PathVariable UUID userId) {
        return ResponseEntity.ok(orderService.getOrdersForUser(userId));
    }
}

