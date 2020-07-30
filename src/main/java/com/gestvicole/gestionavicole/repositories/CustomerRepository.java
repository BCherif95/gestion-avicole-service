package com.gestvicole.gestionavicole.repositories;

import com.gestvicole.gestionavicole.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    boolean existsByName(String name);
    @Query("SELECT pc FROM Customer pc WHERE pc.id <> :id AND pc.name = :name")
    List<Customer> distinctByIdAndExistByName(@Param(value = "id") Long id, @Param(value = "name") String name);
}
