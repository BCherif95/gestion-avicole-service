package com.gestvicole.gestionavicole.controllers;

import com.gestvicole.gestionavicole.entities.Category;
import com.gestvicole.gestionavicole.entities.Customer;
import com.gestvicole.gestionavicole.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<?> create(@RequestBody Category category) {
        return ResponseEntity.ok(categoryService.save(category));
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Category category) {
        return ResponseEntity.ok(categoryService.edit(category));
    }

}
