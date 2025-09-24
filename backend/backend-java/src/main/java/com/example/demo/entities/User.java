package com.example.demo.entities;

import java.util.UUID;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Lombok annotations
import lombok.*;

@Entity
@Table(name = "users")

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder

public class User {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash",nullable = false)
    private String passwordHash;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

}