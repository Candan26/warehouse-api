package com.readingisgood.warehouseapi.service;

import com.readingisgood.warehouseapi.dto.CustomerDto;
import com.readingisgood.warehouseapi.dto.CustomerOrderDto;
import com.readingisgood.warehouseapi.entity.Customer;
import com.readingisgood.warehouseapi.entity.Order;
import com.readingisgood.warehouseapi.mapper.CustomerMapper;
import com.readingisgood.warehouseapi.mapper.impl.CustomerMapperImpl;
import com.readingisgood.warehouseapi.model.WarehouseResponse;
import com.readingisgood.warehouseapi.repository.BookRepository;
import com.readingisgood.warehouseapi.repository.CustomerRepository;
import com.readingisgood.warehouseapi.repository.OrderRepository;
import com.readingisgood.warehouseapi.repository.StockRepository;
import com.readingisgood.warehouseapi.service.impl.CustomerServiceImpl;
import com.readingisgood.warehouseapi.util.WarehouseUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CustomerServiceTest {

    private CustomerRepository customerRepository;

    private OrderRepository orderRepository;

    private CustomerMapper customerMapper;

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerRepository = Mockito.mock(CustomerRepository.class);
        orderRepository = Mockito.mock(OrderRepository.class);
        customerMapper = Mockito.mock(CustomerMapperImpl.class);
        customerService = new CustomerServiceImpl(customerRepository, orderRepository, customerMapper);
    }

    @Test
    public void whenAddCustomerCalledWithValidRequest_itShouldReturnValidDto() {
        Customer customer;
        CustomerDto customerDto;
        customer = getCustomer(WarehouseUtil.VALID);
        customerDto = getCustomerDto(customer);
        Mockito.when(customerRepository.insert(customer)).thenReturn(customer);
        Mockito.when(customerMapper.customerToDto(customer)).thenReturn(customerDto);
        WarehouseResponse warehouseResponse = customerService.addCustomer(customer);
        assertEquals(warehouseResponse.getData(), customerDto);
    }

    @Test
    public void whenAddCustomerCalledWithInValidRequest_itShouldReturnError() {
        Customer customer;
        CustomerDto customerDto;
        customer = getCustomer(WarehouseUtil.FAILED);
        customerDto = getCustomerDto(customer);
        WarehouseResponse warehouseResponse = customerService.addCustomer(customer);
        assertNotEquals(warehouseResponse.getData(), customerDto);
        log.error("Error message ", warehouseResponse.getError().getMessage());
    }

    @Test
    public void whenQueryOrderWithCustomerNameAndSurnameAndTcIdCalledWithInValidRequest_itShouldReturnValidDto() throws Exception {
        Customer customer;
        CustomerOrderDto customerOrderDto;
        List<Customer> customerList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 15);
        List<Order> orderList = new ArrayList<>();
        Page<Order> orderPage;
        Page<CustomerOrderDto> customerOrderDtoPage;

        customer = getCustomer(WarehouseUtil.VALID);
        Order order = getOrderObj(customer);
        orderList.add(order);
        customerList.add(customer);
        orderPage = (Page<Order>) toPage(orderList, pageable);
        customerOrderDto = getCustomerOrderDto(customer, orderPage.getContent().get(0));
        List<CustomerOrderDto> customerOrderDtoList = new ArrayList<>();
        customerOrderDtoList.add(customerOrderDto);
        customerOrderDtoPage = (Page<CustomerOrderDto>) toPage(customerOrderDtoList, pageable);

        Mockito.when(customerRepository.findByNameAndSurname(customer.getName(), customer.getSurname())).thenReturn(customerList);
        Mockito.when(customerRepository.findByTcId(customer.getTcId())).thenReturn(customerList);
        Mockito.when(orderRepository.findByCustomerId(customer.getTcId(), pageable)).thenReturn(orderPage);
        Mockito.when(customerMapper.customerSearchEntityToDto(customer, orderPage)).thenReturn(customerOrderDtoPage);

        //Test by name surname
        WarehouseResponse warehouseResponse = customerService.queryCustomerOrders(customer.getName(), customer.getSurname(), 0, 15);
        assertEquals(warehouseResponse.getData(), customerOrderDtoPage);
        //Test by tc Id
        warehouseResponse = customerService.queryCustomerOrders(customer.getTcId(), 0, 15);
        assertEquals(warehouseResponse.getData(), customerOrderDtoPage);
    }

    public Page<?> toPage(List<?> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        if (start > list.size())
            return new PageImpl<>(new ArrayList<>(), pageable, list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    private Order getOrderObj(Customer customer) {
        Order order = new Order();
        order.setCustomerId(customer.getTcId());
        order.setOrderPrice(13);
        order.setStatus(WarehouseUtil.PURCHASED);
        order.setStartDate(new Date());
        return order;
    }

    private CustomerOrderDto getCustomerOrderDto(Customer customer, Order order) {
        CustomerOrderDto dto = new CustomerOrderDto();
        dto.setOrderDate(order.getStartDate());
        dto.setOrderId(order.getId());
        dto.setCustomerSurname(customer.getTcId());
        dto.setCustomerName(customer.getName());
        dto.setCustomerSurname(customer.getSurname());
        return dto;
    }

    private CustomerDto getCustomerDto(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.setEmail(customer.getEmail());
        dto.setName(customer.getName());
        dto.setAge(customer.getAge());
        dto.setSurname(customer.getSurname());
        return dto;
    }

    private Customer getCustomer(String status) {
        Customer customer = new Customer();
        customer.setTcId(status.equals(WarehouseUtil.VALID) ? "122312312" : null);
        customer.setName(status.equals(WarehouseUtil.VALID) ? "testCustomerName" : null);
        customer.setSurname(status.equals(WarehouseUtil.VALID) ? "testCustomerSurname" : null);
        customer.setEmail("test@testmail.com");
        customer.setAge(0);
        customer.setHasOrder(false);
        return customer;
    }
}