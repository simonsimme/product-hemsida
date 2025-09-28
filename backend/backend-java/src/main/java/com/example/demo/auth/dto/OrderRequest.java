package com.example.demo.auth.dto;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
    @NotNull UUID userId,
    @NotEmpty List<OrderItemRequest> items
) {}
