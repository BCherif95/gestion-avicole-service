package com.gestvicole.gestionavicole.controllers;
import com.gestvicole.gestionavicole.entities.StockOut;
import com.gestvicole.gestionavicole.services.StockOutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock-outs")
public class StockOutController {

    private final StockOutService stockOutService;

    public StockOutController(StockOutService stockOutService) {
        this.stockOutService = stockOutService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(stockOutService.findAll());
    }

    @PostMapping("/save")
    public ResponseEntity<?> create(@RequestBody StockOut stockOut) {
        return ResponseEntity.ok(stockOutService.create(stockOut));
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateStockOut(@RequestBody StockOut stockOut) {
        return ResponseEntity.ok(stockOutService.validateStockOut(stockOut));
    }
}
