package com.readingisgood.warehouseapi.service;

import com.readingisgood.warehouseapi.model.WarehouseResponse;

import java.util.Date;

public interface StatisticsService {
    WarehouseResponse totalOrderCount() throws Exception;

    WarehouseResponse queryCustomerOrders(Date dateBegin, Date dateEnd) throws Exception;
}
