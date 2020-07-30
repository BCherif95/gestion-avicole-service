package com.gestvicole.gestionavicole.repositories;

import com.gestvicole.gestionavicole.entities.Charge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ChargeRepository extends JpaRepository<Charge, Long> {

    @Query("SELECT c from Charge c where DATE(c.date) = DATE(:date)")
    List<Charge> findAllChargeByDate(@Param("date") Date date);

    @Query("SELECT c from Charge c where DATE(c.date) = DATE(:date)")
    Optional<Charge> findByDate(@Param("date") Date date);

    /*@Query("SELECT sum(c.totalLoad) from Charge c where DATE(c.date) = DATE(:date)")
    Integer sumTotalLoadByDate(@Param("date") Date date);

    @Query("SELECT sum(c.netMargin) from Charge c where DATE(c.date) = DATE(:date)")
    Integer sumNetMarginByDate(@Param("date") Date date);*/
}
