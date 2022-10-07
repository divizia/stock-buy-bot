package com.divizia.model.repo;

import com.divizia.model.entity.Order;
import com.divizia.model.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {

    @Query("select b from Order b where b.user.id = :id")
    List<Order> findByUserId(@Param("id") long id);

    @Query(value = "select b from Order b where b.user.id = :id order by b.id desc limit 5", nativeQuery = true)
    List<Order> findByUserIdLast(@Param("id") long id);

    @Query("select b from Order b where b.status = :status")
    List<Order> findByStatus(@Param("status") OrderStatus status);

}
