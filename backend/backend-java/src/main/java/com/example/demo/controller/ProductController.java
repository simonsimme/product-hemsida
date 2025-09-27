package com.example.demo.controller;


import lombok.RequiredArgsConstructor;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import com.example.demo.auth.dto.ProductRequest;
import com.example.demo.entities.Products;
import com.example.demo.service.ProductService;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Products>> getProducts() {
        List<Products> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

        @GetMapping("/{id}")
    public ResponseEntity<Products> getProduct(@PathVariable UUID id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/search")
public ResponseEntity<List<Products>> search(@RequestParam String title) {
    return ResponseEntity.ok(productService.searchProducts(title));
}

   

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Products> createProduct(@RequestBody ProductRequest request) {
        Products product = productService.createProduct(request);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Products> updateProduct(@PathVariable UUID id, @RequestBody ProductRequest request) {
        return productService.updateProduct(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
