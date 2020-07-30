package com.gestvicole.gestionavicole.controllers;

import com.gestvicole.gestionavicole.services.BuildingService;
import com.gestvicole.gestionavicole.wrapper.BuildingSaveEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/buildings")
public class BuildingController {

    private final BuildingService buildingService;

    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getBuildings() {
        return ResponseEntity.ok(buildingService.getBuildings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBuildingById(@PathVariable Long id) {
        return ResponseEntity.ok(buildingService.getBuilding(id));
    }

    @PostMapping("/save")
    public ResponseEntity<?> create(@RequestBody BuildingSaveEntity buildingSaveEntity) {
        return ResponseEntity.ok(buildingService.createBuilding(buildingSaveEntity));
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody BuildingSaveEntity buildingSaveEntity) {
        return ResponseEntity.ok(buildingService.updateBuilding(buildingSaveEntity));
    }

}
