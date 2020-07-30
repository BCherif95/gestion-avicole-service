package com.gestvicole.gestionavicole.controllers;
import com.gestvicole.gestionavicole.entities.StockEntry;
import com.gestvicole.gestionavicole.services.StockEntryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock-entries")
public class StockEntryController {

    private final StockEntryService stockEntryService;

    public StockEntryController(StockEntryService stockEntryService) {
        this.stockEntryService = stockEntryService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(stockEntryService.findAll());
    }

    @PostMapping("/save")
    public ResponseEntity<?> create(@RequestBody StockEntry stockEntry) {
        return ResponseEntity.ok(stockEntryService.create(stockEntry));
    }
}
