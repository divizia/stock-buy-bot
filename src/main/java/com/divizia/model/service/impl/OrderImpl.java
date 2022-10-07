package com.divizia.model.service.impl;

import com.divizia.model.entity.Order;
import com.divizia.model.entity.OrderStatus;
import com.divizia.model.repo.OrderRepo;
import com.divizia.model.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderImpl implements OrderService {

    private OrderRepo orderRepo;

    @Autowired
    public void setOrderRepo(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    @Override
    public Order saveAndFlush(Order order) {
        return orderRepo.saveAndFlush(order);
    }

    @Override
    public void delete(Order order) {
        orderRepo.delete(order);
    }

    @Override
    public void deleteById(long id) {
        orderRepo.deleteById(id);
    }

    @Override
    public Order getById(long id) {
        return orderRepo.getById(id);
    }

    @Override
    public List<Order> findAll() {
        return orderRepo.findAll();
    }

    @Override
    public List<Order> findByUserId(long id) {
        return orderRepo.findByUserId(id);
    }

    @Override
    public List<Order> findByUserIdLast(long id) {
        return orderRepo.findByUserIdLast(id);
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return orderRepo.findByStatus(status);
    }

}
