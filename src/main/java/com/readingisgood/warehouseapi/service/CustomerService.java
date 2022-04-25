package com.readingisgood.warehouseapi.service;

import com.readingisgood.warehouseapi.entity.Customer;
import com.readingisgood.warehouseapi.model.WarehouseResponse;


public interface CustomerService {
    WarehouseResponse queryCustomerOrders(String name, String surname, int page, int size) throws Exception;

    WarehouseResponse queryCustomerOrders(String tc, int page, int size) throws Exception;

    WarehouseResponse addCustomer(Customer customer);
}
