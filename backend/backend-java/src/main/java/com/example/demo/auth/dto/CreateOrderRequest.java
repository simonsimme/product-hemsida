package com.example.demo.auth.dto;

import java.util.List;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;


public record CreateOrderRequest(
    @NotNull UUID userId,
    @NotEmpty List<OrderItemRequest> items
) {}
