package com.example.demo.entities;

import java.util.UUID;
import java.time.OffsetDateTime;

// Lombok annotations
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    private String email;
    private String passwordHash;
    private String role;
    private OffsetDateTime createdAt;
}