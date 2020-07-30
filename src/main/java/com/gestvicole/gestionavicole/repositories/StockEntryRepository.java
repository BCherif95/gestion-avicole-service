package com.gestvicole.gestionavicole.repositories;
import com.gestvicole.gestionavicole.entities.StockEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface StockEntryRepository extends JpaRepository<StockEntry, Long> {

    @Query("SELECT s FROM StockEntry s WHERE DATE(s.date) = DATE(:date) and s.product.id=:id")
    Optional<StockEntry> findByDateAndProductId(@Param("date") Date date, @Param("id") Long id);

    List<StockEntry> findAllByProductId(Long productId);
}
