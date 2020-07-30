package com.gestvicole.gestionavicole.repositories;
import com.gestvicole.gestionavicole.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByDesignation(String designation);

    @Query("SELECT p FROM Product p WHERE p.id <> :id AND p.designation = :designation")
    List<Product> distinctByIdExistAndDescription(@Param(value = "id") Long id, @Param(value = "designation") String designation);
}
