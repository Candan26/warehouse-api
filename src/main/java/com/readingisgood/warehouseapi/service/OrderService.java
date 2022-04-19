package com.readingisgood.warehouseapi.service;

import com.readingisgood.warehouseapi.entity.Order;
import com.readingisgood.warehouseapi.model.WarehouseResponse;

import java.util.Date;

public interface OrderService {
    WarehouseResponse queryOrdersByDateInterval(Date startDate, Date stopDate);

    WarehouseResponse queryOrdersById(String id);

    WarehouseResponse addNewOrder(Order request);
}
