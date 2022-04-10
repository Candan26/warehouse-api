package com.readingisgood.warehouseapi.service.impl;

import com.readingisgood.warehouseapi.dto.CustomerOrderDto;
import com.readingisgood.warehouseapi.entity.Customer;
import com.readingisgood.warehouseapi.entity.Order;
import com.readingisgood.warehouseapi.mapper.CustomerMapper;
import com.readingisgood.warehouseapi.model.Error;
import com.readingisgood.warehouseapi.model.WarehouseResponse;
import com.readingisgood.warehouseapi.repository.CustomerRepository;
import com.readingisgood.warehouseapi.repository.OrderRepository;
import com.readingisgood.warehouseapi.service.CustomerService;
import com.readingisgood.warehouseapi.util.WarehouseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private static final String ERROR_WRONG_CUSTOMER_DATA = "Please write customer name, surname and tc_id number";

    private final CustomerRepository customerRepository;

    private final OrderRepository orderRepository;

    private final CustomerMapper customerMapper;


    @Override
    public Page<CustomerOrderDto> queryCustomerOrders(String name, String surname, int page, int size) throws Exception {
        Pageable pagable = PageRequest.of(page, size);
        Customer customer = null;
        Page<Order> orderList = null;
        List<Customer> customerList = customerRepository.findByNameAndSurname(name, surname);
        if (!customerList.isEmpty()) {
            customer = customerList.get(0);
        }
        if(customer !=null){
            orderList = orderRepository.findByCustomerId(customer.getTcId(), pagable);
        }
        return  customerMapper.customerSearchEntityToDto(customer,orderList);
    }

    @Override
    public Page<CustomerOrderDto> queryCustomerOrders(String tc, int page, int size) throws Exception {
        Pageable pagable = PageRequest.of(page, size);
        Customer customer = null;
        Page<Order> orderList = null;
        List<Customer> customerList = customerRepository.findByTcId(tc);
        if (!customerList.isEmpty()) {
            customer = customerList.get(0);
        }
        if(customer !=null){
            orderList = orderRepository.findByCustomerId(customer.getTcId(), pagable);
        }
        return  customerMapper.customerSearchEntityToDto(customer,orderList);
    }

    @Override
    public WarehouseResponse addCustomer(Customer customer) {
        try {
            if (Stream.of(customer).anyMatch(x -> (x == null || (x.getName() == null || x.getName().equals("")
                    || (x.getSurname() == null || x.getSurname().equals("")
                    || (x.getTcId() == null || x.getTcId().equals(""))))))) {
                return new WarehouseResponse(WarehouseUtil.FAILED, "", new Error(HttpStatus.BAD_REQUEST, ERROR_WRONG_CUSTOMER_DATA));
            }
            customerRepository.insert(customer);
            return new WarehouseResponse(WarehouseUtil.SUCCEED, customer, null);
        } catch (Exception ex) {
            return new WarehouseResponse(WarehouseUtil.FAILED, "", new Error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex));
        }
    }
}
