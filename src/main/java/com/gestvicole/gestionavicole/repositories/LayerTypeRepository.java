package com.gestvicole.gestionavicole.repositories;

import com.gestvicole.gestionavicole.entities.LayerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LayerTypeRepository extends JpaRepository<LayerType, Long> {
    List<LayerType> findAllByBuildingId(@Param("buildingId") Long buildingId);
}
