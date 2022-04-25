package com.readingisgood.warehouseapi.service.impl;

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

    private static final String ERROR_NO_CONTENT_CUSTOMER = "cannot found customers with this parameters";

    private static final String ERROR_NO_CONTENT_ORDER = "cannot found orders in given customer";

    private final CustomerRepository customerRepository;

    private final OrderRepository orderRepository;

    private final CustomerMapper customerMapper;


    @Override
    public WarehouseResponse queryCustomerOrders(String name, String surname, int page, int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size);
        Customer customer;
        Page<Order> orderList;
        List<Customer> customerList = customerRepository.findByNameAndSurname(name, surname);
        if (customerList.isEmpty()) {
            log.debug("Customer list is empty for ordering query");
            return new WarehouseResponse(WarehouseUtil.FAILED, "", new Error(HttpStatus.NO_CONTENT, ERROR_NO_CONTENT_CUSTOMER));
        }
        customer = customerList.get(0);
        log.debug("Customer object found searching for order list from name "+name+" surname "+ surname);
        orderList = orderRepository.findByCustomerId(customer.getTcId(), pageable);
        if (orderList.isEmpty()) {
            log.debug("Order list is empty in give customer id " + customer.getTcId());
            return new WarehouseResponse(WarehouseUtil.FAILED, "", new Error(HttpStatus.NO_CONTENT, ERROR_NO_CONTENT_ORDER));
        }
        return new WarehouseResponse(WarehouseUtil.SUCCEED, customerMapper.customerSearchEntityToDto(customer, orderList), null);
    }

    @Override
    public WarehouseResponse queryCustomerOrders(String tc, int page, int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size);
        Customer customer;
        Page<Order> orderList;
        List<Customer> customerList = customerRepository.findByTcId(tc);
        if (customerList.isEmpty()) {
            log.debug("Customer list empty for given tc id "+ tc);
            return new WarehouseResponse(WarehouseUtil.FAILED, "", new Error(HttpStatus.NO_CONTENT, ERROR_NO_CONTENT_CUSTOMER));
        }
        customer = customerList.get(0);
        orderList = orderRepository.findByCustomerId(customer.getTcId(), pageable);
        return new WarehouseResponse(WarehouseUtil.SUCCEED, customerMapper.customerSearchEntityToDto(customer, orderList), null);
    }

    @Override
    public WarehouseResponse addCustomer(Customer customer) {
        try {
            if (Stream.of(customer).anyMatch(x -> (x == null || (x.getName() == null || x.getName().equals("")
                    || (x.getSurname() == null || x.getSurname().equals("")
                    || (x.getTcId() == null || x.getTcId().equals(""))))))) {
                log.error("The customer object has wrong formatted name:  " + (customer != null ? customer.getName() : null) +
                        " or surname: " + (customer != null ? customer.getSurname() : null) +
                        " or tcid: " + (customer != null ? customer.getTcId() : null));
                return new WarehouseResponse(WarehouseUtil.FAILED, "", new Error(HttpStatus.BAD_REQUEST, ERROR_WRONG_CUSTOMER_DATA));
            }
            customerRepository.insert(customer);
            return new WarehouseResponse(WarehouseUtil.SUCCEED, customerMapper.customerToDto(customer), null);
        } catch (Exception ex) {
            log.error("Exception on ", ex);
            return new WarehouseResponse(WarehouseUtil.FAILED, "", new Error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex));
        }
    }
}
