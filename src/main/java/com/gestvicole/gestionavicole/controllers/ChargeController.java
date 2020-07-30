package com.gestvicole.gestionavicole.controllers;

import com.gestvicole.gestionavicole.entities.Charge;
import com.gestvicole.gestionavicole.entities.ChargeProduction;
import com.gestvicole.gestionavicole.repositories.ChargeProductionRepository;
import com.gestvicole.gestionavicole.services.ChargeService;
import com.gestvicole.gestionavicole.utils.ResponseBody;
import com.gestvicole.gestionavicole.utils.SearchBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/charges")
public class ChargeController {
    private final ChargeService chargeService;

    public ChargeController(ChargeService chargeService) {
        this.chargeService = chargeService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(chargeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCharge(@PathVariable Long id){
        return ResponseEntity.ok(chargeService.getCharge(id));
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<?> getChargeInfo(@PathVariable Long id){
        return ResponseEntity.ok(chargeService.getGroupInfos(id));
    }

    @PostMapping("/save")
    public ResponseEntity<?> create(@RequestBody Charge charge) {
        return ResponseEntity.ok(chargeService.create(charge));
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Charge charge) {
        return ResponseEntity.ok(chargeService.update(charge));
    }

    @PostMapping("/dashboard")
    public ResponseEntity<?> initChargeDash(@RequestBody SearchBody searchBody) {
        return ResponseEntity.ok(chargeService.initChargeDash(searchBody));
    }
}
