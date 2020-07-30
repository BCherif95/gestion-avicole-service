package com.gestvicole.gestionavicole.controllers;

import com.gestvicole.gestionavicole.entities.Role;
import com.gestvicole.gestionavicole.services.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(roleService.getAll());
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<?> getByName(@PathVariable String name) {
        return ResponseEntity.ok(roleService.getByName(name));
    }

    @PostMapping("/save")
    public ResponseEntity<?> create(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.save(role));
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.save(role));
    }
}
