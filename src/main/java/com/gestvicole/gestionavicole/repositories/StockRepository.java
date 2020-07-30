package com.gestvicole.gestionavicole.repositories;
import com.gestvicole.gestionavicole.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByProductId(Long productId);

    @Query("SELECT s from Stock s where s.product.id=:id and s.solde >=:qte")
    Optional<Stock> checkBalance(@Param("id") Long id,@Param("qte") Integer qte);
}
