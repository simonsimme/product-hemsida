package com.example.demo.entities;

import java.util.UUID;
import java.time.OffsetDateTime;

// Lombok annotations
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Products {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    private String title;
    private String description;
    private Double price;
    private Integer quantity;
    private String imageUrl;
    private OffsetDateTime createdAt;
}