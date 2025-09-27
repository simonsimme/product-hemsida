package com.example.demo.auth.dto;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;



public record OrderItemRequest(
@NotNull UUID productId,
@Min(1) int quantity
) {}

