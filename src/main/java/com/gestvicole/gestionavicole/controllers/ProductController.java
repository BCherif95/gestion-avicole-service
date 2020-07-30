package com.gestvicole.gestionavicole.controllers;

import com.gestvicole.gestionavicole.entities.Product;
import com.gestvicole.gestionavicole.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<?> create(@RequestBody Product product) {
        return ResponseEntity.ok(productService.save(product));
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Product product) {
        return ResponseEntity.ok(productService.edit(product));
    }
}
