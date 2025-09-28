package com.example.demo.entities;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

// Lombok annotations
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItem {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    
    @JsonIgnore
    private Order order;
    
    private Products product;
    private Integer quantity;
    private Double price;
}