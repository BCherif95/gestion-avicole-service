package com.gestvicole.gestionavicole.repositories;

import com.gestvicole.gestionavicole.entities.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ParameterRepository extends JpaRepository<Parameter, Long> {
    Parameter findParameterById(@Param(value = "id") Long id);
}
