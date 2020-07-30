package com.gestvicole.gestionavicole.controllers;

import com.gestvicole.gestionavicole.services.LayerTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/layer_types")
public class LayerTypeController {

    private final LayerTypeService layerTypeService;

    public LayerTypeController(LayerTypeService layerTypeService) {
        this.layerTypeService = layerTypeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLayersTypeByBuildingId(@PathVariable Long id) {
        return ResponseEntity.ok(layerTypeService.findAllByBuildingId(id));
    }
}
