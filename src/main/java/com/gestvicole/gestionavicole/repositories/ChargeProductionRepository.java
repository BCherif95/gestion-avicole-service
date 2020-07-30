package com.gestvicole.gestionavicole.repositories;

import com.gestvicole.gestionavicole.entities.ChargeProduction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChargeProductionRepository extends JpaRepository<ChargeProduction, Long> {

    List<ChargeProduction> findAllByChargeIdIn(List<Long> ids);

    List<ChargeProduction> findByChargeId(Long chargeId);
}
