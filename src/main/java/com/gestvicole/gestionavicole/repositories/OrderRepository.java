package com.gestvicole.gestionavicole.repositories;
import com.gestvicole.gestionavicole.entities.Order;
import com.gestvicole.gestionavicole.utils.Enumeration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o from Order o where o.state=:state")
    List<Order> findAllOrderByState(@Param(value = "state") Enumeration.ORDER_STATE state);
    List<Order> findAllByCustomerId(@Param(value = "id") Long id);
    @Query("SELECT o from Order o where DATE(o.orderDate) = DATE(:orderDate)")
    List<Order> findAllOrderByDate(@Param("orderDate") Date orderDate);
}
