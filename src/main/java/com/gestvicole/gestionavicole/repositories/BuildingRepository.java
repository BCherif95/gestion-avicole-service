package com.gestvicole.gestionavicole.repositories;

import com.gestvicole.gestionavicole.entities.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    boolean existsByName(String name);
    @Query("SELECT b FROM Building b WHERE b.id <> :id AND b.name = :name")
    List<Building> distinctByIdAndExistByName(@Param(value = "id") Long id, @Param(value = "name") String name);
}
