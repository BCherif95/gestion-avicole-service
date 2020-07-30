package com.gestvicole.gestionavicole.controllers;

import com.gestvicole.gestionavicole.entities.User;
import com.gestvicole.gestionavicole.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> listOfUsers() {
        return ResponseEntity.ok(userService.listOfUsers());
    }

    @PostMapping("/save")
    public ResponseEntity<?> create(@RequestBody User user) {
        return ResponseEntity.ok(userService.create(user));
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody User user) {
        return ResponseEntity.ok(userService.edit(user));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(userService.remove(id));
    }

    @GetMapping("/{id}/getUser")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }


}
