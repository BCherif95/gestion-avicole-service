package com.gestvicole.gestionavicole.controllers;

import com.gestvicole.gestionavicole.entities.Order;
import com.gestvicole.gestionavicole.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/all")
    private ResponseEntity<?> findAll(){
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @GetMapping("/by-waiting")
    private ResponseEntity<?> findAllOrderWaiting() {
        return ResponseEntity.ok(orderService.findAllOrderWaiting());
    }

    @PostMapping("/save")
    private ResponseEntity<?> create(@RequestBody Order order){
        return ResponseEntity.ok(orderService.create(order));
    }

    @PutMapping("/update")
    private ResponseEntity<?> update(@RequestBody Order order){
        return ResponseEntity.ok(orderService.edit(order));
    }
}
