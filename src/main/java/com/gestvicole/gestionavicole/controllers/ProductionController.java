package com.gestvicole.gestionavicole.controllers;

import com.gestvicole.gestionavicole.entities.Production;
import com.gestvicole.gestionavicole.services.ProductionService;
import com.gestvicole.gestionavicole.utils.SearchBody;
import com.gestvicole.gestionavicole.utils.SearchDateBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/productions")
public class ProductionController {

    private final ProductionService productionService;

    public ProductionController(ProductionService productionService) {
        this.productionService = productionService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getProductions() {
        return ResponseEntity.ok(productionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduction(@PathVariable Long id) {
        return ResponseEntity.ok(productionService.getProduction(id));
    }

    @GetMapping("/all-day")
    public ResponseEntity<?> getAllProdOfDay() {
        return ResponseEntity.ok(productionService.getAllProdOfDay());
    }

    @PostMapping("/findBy-Date")
    public ResponseEntity<?> findByBuildingIdAndDate(@RequestBody SearchBody searchBody){
        return ResponseEntity.ok(productionService.findByBuildingIdAndDate(searchBody));
    }

    @PostMapping("/save")
    public ResponseEntity<?> create(@RequestBody Production production) {
        return ResponseEntity.ok(productionService.create(production));
    }

    @PostMapping("/between")
    public ResponseEntity<?> findByDateBetween(@RequestBody SearchDateBody searchDateBody) {
        return ResponseEntity.ok(productionService.findByDateBetween(searchDateBody));
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Production production) {
        return ResponseEntity.ok(productionService.update(production));
    }

    @PostMapping("/dashboard")
    public ResponseEntity<?> initProdDash(@RequestBody SearchBody searchBody) {
        return ResponseEntity.ok(productionService.initProdDash(searchBody));
    }

}
