package com.gestvicole.gestionavicole.repositories;

import com.gestvicole.gestionavicole.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository  extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

    @Query("SELECT cat FROM Category cat WHERE cat.id <> :id AND cat.name = :name")
    List<Category> distinctByIdAndExistByName(@Param(value = "id") Long id, @Param(value = "name") String name);
}
