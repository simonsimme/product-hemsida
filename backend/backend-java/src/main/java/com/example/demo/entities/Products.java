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
@Table(name = "products")

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Products {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description",nullable = false)
    private String description;

    @Column(name = "price",nullable = false)
    private Double price;

    @Column(name = "quantity",nullable = false)
    private Integer quantity;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

}