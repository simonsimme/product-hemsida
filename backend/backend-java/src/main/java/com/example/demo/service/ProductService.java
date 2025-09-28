package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.demo.auth.dto.ProductRequest;
import com.example.demo.entities.Products;
import com.example.demo.repos.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Products> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Products> getProductById(UUID id) {
        return productRepository.findById(id);
    }
    public List<Products> searchProducts(String title) {
    return productRepository.findByTitle(title);
}

    public Products createProduct(ProductRequest request) {
    Products product = new Products();
    product.setId(UUID.randomUUID());
    product.setTitle(request.title());
    product.setDescription(request.description());
    product.setImageUrl(request.imageUrl());
    product.setQuantity(request.quantity());
    product.setPrice(request.price());
    return productRepository.save(product);
    }

    public Optional<Products> updateProduct(UUID id, ProductRequest request) {
        return productRepository.findById(id).map(product -> {
            product.setTitle(request.title());
            product.setDescription(request.description());
            product.setImageUrl(request.imageUrl());
            product.setQuantity(request.quantity());
            product.setPrice(request.price());
            return productRepository.save(product);
        });
    }

    public boolean deleteProduct(UUID id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
