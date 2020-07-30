package com.gestvicole.gestionavicole.repositories;
import com.gestvicole.gestionavicole.entities.StockOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface StockOutRepository extends JpaRepository<StockOut, Long> {

    @Query("SELECT s FROM StockOut s WHERE DATE(s.date) = DATE(:date) and s.product.id=:id")
    Optional<StockOut> findByDateAndProductId(@Param("date") Date date, @Param("id") Long id);

    List<StockOut> findAllByProductId(Long productId);
}
