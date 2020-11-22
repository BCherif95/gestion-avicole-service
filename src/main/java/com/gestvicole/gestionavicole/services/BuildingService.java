package com.gestvicole.gestionavicole.services;

import com.gestvicole.gestionavicole.entities.Building;
import com.gestvicole.gestionavicole.entities.LayerType;
import com.gestvicole.gestionavicole.repositories.BuildingRepository;
import com.gestvicole.gestionavicole.utils.ResponseBody;
import com.gestvicole.gestionavicole.wrapper.BuildingSaveEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.partitioningBy;


@Service
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final LayerTypeService layerTypeService;

    public BuildingService(BuildingRepository buildingRepository, LayerTypeService layerTypeService) {
        this.buildingRepository = buildingRepository;
        this.layerTypeService = layerTypeService;
    }

    public ResponseBody getBuildings() {
        try {
            List<Building> list = buildingRepository.findAll();
            list.sort(Collections.reverseOrder());
            return ResponseBody.with(list, "Liste des Batiments");
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody getBuilding(Long id) {
        try {
            Optional<Building> building = buildingRepository.findById(id);
            return building.map(building1 -> ResponseBody.with(building1, "Batiment recuperer avec succès"))
                    .orElseGet(() -> ResponseBody.error("Cet Batiment n'existe pas"));
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody createBuilding(BuildingSaveEntity buildingSaveEntity) {

        try {
            if (isEmpty(buildingSaveEntity.getLayerTypes())) {
                return ResponseBody.error("Liste vide!");
            }
            if (buildingRepository.existsByName(buildingSaveEntity.getBuilding().getName())) {
                return ResponseBody.error("Ce nom existe deja!");
            }

            //Total Layers
            assingTotalLayer(buildingSaveEntity.getBuilding(), buildingSaveEntity.getLayerTypes());

            //Building
            Building newBuilding = buildingRepository.saveAndFlush(buildingSaveEntity.getBuilding());

            //Type Layers
            buildingSaveEntity.getLayerTypes()
                    .stream()
                    .peek(layerType -> setterBuildind(layerType,newBuilding))
                    .forEach(this::saveLargeType);
            return ResponseBody.with(newBuilding, "Ajouter avec succès");

        }catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue!");
        }

    }

    public ResponseBody updateBuilding(BuildingSaveEntity buildingSaveEntity) {
        try {
            if (isEmpty(buildingSaveEntity.getLayerTypes())) {
                return ResponseBody.error("Liste vide!");
            }
            boolean isExist = buildingRepository.distinctByIdAndExistByName(buildingSaveEntity.getBuilding().getId(), buildingSaveEntity.getBuilding().getName()).isEmpty();
            if (!isExist) {
                return ResponseBody.error("Ce nom existe deja!");
            }
            //Total Layers
            assingTotalLayer(buildingSaveEntity.getBuilding(), buildingSaveEntity.getLayerTypes());

            //Building
            Building newBuilding = buildingRepository.saveAndFlush(buildingSaveEntity.getBuilding());

            //Types
            List<LayerType> layerTypes = layerTypeService.findAllByBuildingId(newBuilding.getId());

            List<Long> typesExisting = buildingSaveEntity.getLayerTypes()
                    .stream()
                    .filter(layerType -> layerType.getId() !=null)
                    .map(LayerType::getId)
                    .collect(Collectors.toList());

            this.layerTypeService.deleteAllType(
                    Optional.ofNullable(
                            layerTypes
                                    .stream()
                                    .collect(partitioningBy(p -> typesExisting.contains(p.getId())))
                                    .get(false)
                    ).orElse(new ArrayList())
            );
            //Type Layers
            buildingSaveEntity.getLayerTypes()
                    .stream()
                    .peek(layerType -> setterBuildind(layerType,newBuilding))
                    .forEach(this::saveLargeType);
            return ResponseBody.with(newBuilding, "Modifier avec succès");


        }catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    private void assingTotalLayer(Building building, List<LayerType> layerTypes) {
        building.setTotalLayer(
                Optional.of(layerTypes
                .stream()
                .map(LayerType::getQuantity)
                .reduce(0,Integer::sum))
                .orElse(0)
        );

    }

    private void saveLargeType(LayerType layerType) {
        this.layerTypeService.create(layerType);
    }

    private void setterBuildind(LayerType layerType, Building building) {
        layerType.setBuilding(building);
    }

    private boolean isEmpty(Collection<LayerType> collections) {
        return collections == null || collections.isEmpty();
    }
}
