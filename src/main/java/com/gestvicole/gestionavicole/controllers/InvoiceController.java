package com.gestvicole.gestionavicole.controllers;

import com.gestvicole.gestionavicole.entities.Invoice;
import com.gestvicole.gestionavicole.services.InvoiceService;
import com.gestvicole.gestionavicole.utils.SearchBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/all")
    private ResponseEntity<?> findAll(){
        return ResponseEntity.ok(invoiceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoice(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getInvoice(id));
    }

    @PostMapping("/save")
    private ResponseEntity<?> create(@RequestBody Invoice invoice){
        return ResponseEntity.ok(invoiceService.create(invoice));
    }

    @PutMapping("/update")
    private ResponseEntity<?> update(@RequestBody Invoice invoice){
        return ResponseEntity.ok(invoiceService.update(invoice));
    }
    @PostMapping("/dashboard")
    public ResponseEntity<?> initInvoiceDash(@RequestBody SearchBody searchBody) {
        return ResponseEntity.ok(invoiceService.initInvoiceDash(searchBody));
    }
}
