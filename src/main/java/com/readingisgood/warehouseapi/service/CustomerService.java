package com.readingisgood.warehouseapi.service;

import com.readingisgood.warehouseapi.dto.CustomerDto;
import com.readingisgood.warehouseapi.dto.CustomerOrderDto;
import com.readingisgood.warehouseapi.entity.Customer;
import com.readingisgood.warehouseapi.model.WarehouseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

public interface CustomerService {
    Page<CustomerOrderDto> queryCustomerOrders(String name,  String surname, int page, int size) throws Exception;
    WarehouseResponse queryCustomerOrders(String tc, int page, int size) throws Exception;
    WarehouseResponse addCustomer(Customer customer);
}
