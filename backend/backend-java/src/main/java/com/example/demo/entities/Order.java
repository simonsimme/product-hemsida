package com.example.demo.entities;

import java.util.UUID;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
// Lombok annotations
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Order {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    
    @JsonIgnore
    private User user;
    
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();
    
    private OffsetDateTime createdAt;
    
    @Builder.Default
    private String status = "ACTIVE";
}