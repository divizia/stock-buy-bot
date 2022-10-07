package com.divizia.model.service;

import com.divizia.model.entity.Order;
import com.divizia.model.entity.OrderStatus;
import org.springframework.lang.Nullable;

import java.util.List;

public interface OrderService {

    Order saveAndFlush(Order order);
    void delete(Order order);
    void deleteById(long id);
    @Nullable
    Order getById(long id);
    List<Order> findAll();
    List<Order> findByUserId(long id);
    List<Order> findByUserIdLast(long id);
    List<Order> findByStatus(OrderStatus status);

}
