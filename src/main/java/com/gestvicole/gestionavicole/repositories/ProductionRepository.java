package com.gestvicole.gestionavicole.repositories;

import com.gestvicole.gestionavicole.entities.Production;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ProductionRepository extends JpaRepository<Production, Long> {

    @Query("SELECT p from Production p where DATE(p.date) = DATE(:date) and p.building.id=:id")
    Production findByBuildingIdAndDate(@Param("date") Date date,@Param("id") Long id);

    @Query("SELECT p from Production p where DATE(p.date) = DATE(:date)")
    List<Production> findAllByDate(@Param("date") Date date);

    @Query("SELECT p from Production p where DATE(p.date) = DATE(:date) and p.building.id=:id")
    List<Production> findAllByDateAndBuildingId(@Param("date") Date date,@Param("id") Long id);

    @Query("SELECT sum(p.overallProduction) from Production p where DATE(p.date) = DATE(:date) and p.building.id=:id")
    Integer sumTotalOverallProductionByDateAndBuildingId(@Param("date") Date date,@Param("id") Long id);

    @Query("SELECT sum(p.generalEffective) from Production p where DATE(p.date) = DATE(:date) and p.building.id=:id")
    Integer sumTotalEffectiveByDateAndBuildingId(@Param("date") Date date,@Param("id") Long id);

    @Query("SELECT p from Production p where DATE(p.date) >= DATE(:date1) and DATE(p.date) <= DATE(:date2)")
    List<Production> findProdByDateBetween(@Param("date1") Date date1,@Param("date2") Date date2);

}
