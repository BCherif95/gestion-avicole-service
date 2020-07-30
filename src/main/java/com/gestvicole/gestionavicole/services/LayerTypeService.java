package com.gestvicole.gestionavicole.services;

import com.gestvicole.gestionavicole.entities.LayerType;
import com.gestvicole.gestionavicole.repositories.LayerTypeRepository;
import com.gestvicole.gestionavicole.utils.ResponseBody;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LayerTypeService {

    private final LayerTypeRepository layerTypeRepository;

    public LayerTypeService(LayerTypeRepository layerTypeRepository) {
        this.layerTypeRepository = layerTypeRepository;
    }

    public ResponseBody create(LayerType layerType) {
        try {
            return ResponseBody.with(layerTypeRepository.save(layerType), "Ajouter avec succès");
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue!");
        }
    }

    public List<LayerType> findAllByBuildingId(Long id){
        return layerTypeRepository.findAllByBuildingId(id);
    }

    public ResponseBody findTypeById(Long id) {
        try {
            Optional<LayerType> typeOptional = layerTypeRepository.findById(id);
            return typeOptional.map(type -> ResponseBody.with(type, "Type recuperer avec succès")).orElseGet(() -> ResponseBody.error("Ce Type n'existe pas"));
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public void deleteAllType(List<LayerType> layerTypes) {
        layerTypeRepository.deleteAll(layerTypes);
    }
}


