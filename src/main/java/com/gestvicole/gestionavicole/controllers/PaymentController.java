package com.gestvicole.gestionavicole.controllers;

import com.gestvicole.gestionavicole.entities.Payment;
import com.gestvicole.gestionavicole.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/all")
    private ResponseEntity<?> findAll() {
        return ResponseEntity.ok(paymentService.findAll());
    }

    @PostMapping("/save")
    private ResponseEntity<?> create(@RequestBody Payment payment){
        return ResponseEntity.ok(paymentService.create(payment));
    }

    @PutMapping("/update")
    private ResponseEntity<?> update(@RequestBody Payment payment){
        return ResponseEntity.ok(paymentService.update(payment));
    }

    @PutMapping("/cancel")
    private ResponseEntity<?> cancel(@RequestBody Payment payment){
        return ResponseEntity.ok(paymentService.cancelPayment(payment));
    }
}
