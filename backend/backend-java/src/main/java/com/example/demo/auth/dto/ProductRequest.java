package com.example.demo.auth.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotBlank;


public record ProductRequest(
@NotBlank String title,
@NotBlank String description,
@Positive double price,
@Min(0) int quantity,
String imageUrl
) {}