package com.readingisgood.warehouseapi.repository;

import com.readingisgood.warehouseapi.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface OrderRepository   extends MongoRepository<Order, String> {
    Order findByOrderNumber(int orderNumber);
    Page<Order> findByCustomerId(String customerId, Pageable pageable);
    List<Order> findByStartDateBetween(Date startDate, Date stopDate);
}
