package com.readingisgood.warehouseapi.service;

import com.readingisgood.warehouseapi.dto.OrderDto;
import com.readingisgood.warehouseapi.entity.Book;
import com.readingisgood.warehouseapi.entity.Customer;
import com.readingisgood.warehouseapi.entity.Order;
import com.readingisgood.warehouseapi.entity.Stock;
import com.readingisgood.warehouseapi.mapper.CustomerMapper;
import com.readingisgood.warehouseapi.model.WarehouseResponse;
import com.readingisgood.warehouseapi.repository.OrderRepository;
import com.readingisgood.warehouseapi.repository.StockRepository;
import com.readingisgood.warehouseapi.service.impl.OrderServiceImpl;
import com.readingisgood.warehouseapi.util.WarehouseUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Or;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class OrderServiceTest {


    private OrderService orderService;
    private OrderRepository orderRepository;
    private StockRepository stockRepository;
    private CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        orderRepository = Mockito.mock(OrderRepository.class);
        stockRepository = Mockito.mock(StockRepository.class);
        customerMapper = Mockito.mock(CustomerMapper.class);
        orderService = new OrderServiceImpl(orderRepository, stockRepository, customerMapper);
    }


    @Test
    public void whenAddNewOrderCalledWithValidRequest_itShouldReturnValidDto() {
        Book book = BookServiceTest.getBook(WarehouseUtil.VALID);
        Stock stock = BookServiceTest.getStock(book);
        Customer customer = CustomerServiceTest.getCustomer(WarehouseUtil.VALID);
        Order order = getOrderObj(customer,book);
        OrderDto orderDto = getOrderDto(order);
        Mockito.when(stockRepository.findByBookName(Mockito.any())).thenReturn(stock);
        Mockito.when(stockRepository.save(stock)).thenReturn(stock);
        Mockito.when(orderRepository.save(order)).thenReturn(order);
        Mockito.when(customerMapper.orderToDto(Mockito.any())).thenReturn(orderDto);
        WarehouseResponse warehouseResponse = orderService.addNewOrder(order);
        assertEquals(warehouseResponse.getData(), orderDto);
    }

    @Test
    public void whenAddNewOrderCalledWithEmptyBookList_itShouldReturnValidError() {
        Customer customer = CustomerServiceTest.getCustomer(WarehouseUtil.VALID);
        Book book = BookServiceTest.getBook(WarehouseUtil.VALID);
        Order order = getOrderObj(customer,book);
        Mockito.when(stockRepository.findByBookName(Mockito.any())).thenReturn(new Stock());
        WarehouseResponse warehouseResponse = orderService.addNewOrder(order);
        assertNotEquals(warehouseResponse.getData(), order);
        log.error("Error message " + warehouseResponse.getError().getMessage());
    }

    @Test
    public void whenAddNewOrderCalledWithAddBookWhichIsNotInStock_itShouldReturnValidError() {
        Customer customer = CustomerServiceTest.getCustomer(WarehouseUtil.VALID);
        Order order = getOrderObj(customer);
        WarehouseResponse warehouseResponse = orderService.addNewOrder(order);
        assertNotEquals(warehouseResponse.getData(), order);
        log.error("Error message " + warehouseResponse.getError().getMessage());
    }

    @Test
    public void whenAddNewOrderCalledWithAddBookWhenCannotUpdate_itShouldReturnValidError() {
        Customer customer = CustomerServiceTest.getCustomer(WarehouseUtil.VALID);
        Order order = getOrderObj(customer);
        Mockito.when(stockRepository.save(Mockito.any())).thenThrow(new IllegalArgumentException());
        WarehouseResponse warehouseResponse = orderService.addNewOrder(order);
        assertNotEquals(warehouseResponse.getData(), order);
        log.error("Error message " + warehouseResponse.getError().getMessage());
    }


    @Test
    public void whenQueryOrdersByDateIntervalCalledWithValidRequest_itShouldReturnValidDto() {
        Customer customer = CustomerServiceTest.getCustomer(WarehouseUtil.VALID);
        Order order = getOrderObj(customer);
        OrderDto orderDto = getOrderDto(order);
        List<Order> orderList = new ArrayList<>();
        List<OrderDto> orderDtoList = new ArrayList<>();
        orderDtoList.add(orderDto);
        orderList.add(order);
        Date dateBegin = new Date();
        Date dateEnd = new Date();
        Mockito.when(orderRepository.findByStartDateBetween(dateBegin, dateEnd)).thenReturn(orderList);
        Mockito.when(customerMapper.orderToDto(Mockito.any())).thenReturn(orderDto);
        WarehouseResponse warehouseResponse =  orderService.queryOrdersByDateInterval(dateBegin, dateEnd);
        assertEquals(warehouseResponse.getData(), orderDtoList);
    }

    @Test
    public void whenQueryOrdersByDateIntervalCalledWithEmptyOrder_itShouldReturnValidError() {
        Date dateBegin = new Date();
        Date dateEnd = new Date();
        Mockito.when(orderRepository.findByStartDateBetween(dateBegin, dateEnd)).thenReturn(null);
        WarehouseResponse warehouseResponse =  orderService.queryOrdersByDateInterval(dateBegin, dateEnd);
        assertNotEquals(warehouseResponse.getData(), null);
        log.error("Error message " + warehouseResponse.getError().getMessage());
    }

    @Test
    public void whenQueryOrdersByDateIntervalCalledWithExceptionOnFindByStartDateBetween_itShouldReturnValidError() {
        Date dateBegin = new Date();
        Date dateEnd = new Date();
        Mockito.when(orderRepository.findByStartDateBetween(dateBegin, dateEnd)).thenThrow(new IllegalArgumentException());
        WarehouseResponse warehouseResponse =  orderService.queryOrdersByDateInterval(dateBegin, dateEnd);
        assertNotEquals(warehouseResponse.getData(), null);
        log.error("Error message " + warehouseResponse.getError().getMessage());
    }

    @Test
    public void whenQueryOrdersByIdCalledWithValidRequest_itShouldReturnValidDto() {
        Customer customer = CustomerServiceTest.getCustomer(WarehouseUtil.VALID);
        Order order = getOrderObj(customer);
        OrderDto orderDto = getOrderDto(order);
        Optional<Order> optionalOrder = Optional.of(order);
        Mockito.when(orderRepository.findById("123")).thenReturn(optionalOrder);
        Mockito.when(customerMapper.orderToDto(Mockito.any())).thenReturn(orderDto);
        WarehouseResponse warehouseResponse =  orderService.queryOrdersById("123");
        assertEquals(warehouseResponse.getData(), orderDto);
    }

    @Test
    public void whenQueryOrdersByIdCalledWithInValidRequest_itShouldReturnError() {
        Mockito.when(orderRepository.findById("123")).thenReturn(null);
        WarehouseResponse warehouseResponse =  orderService.queryOrdersById("123");
        assertNotEquals(warehouseResponse.getData(), null);
        log.error("Error message "+ warehouseResponse.getError().getMessage());
    }

    public static OrderDto getOrderDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setOrderNumber(order.getOrderNumber());
        dto.setBookList(order.getBookList());
        dto.setOrderPrice(order.getOrderPrice());
        dto.setStatus(order.getStatus());
        dto.setStartDate(order.getStartDate());
        dto.setCustomerId(order.getCustomerId());
        return dto;
    }

    public static Order getOrderObj(Customer customer) {
        Order order = new Order();
        order.setCustomerId(customer.getTcId());
        order.setOrderPrice(13);
        order.setStatus(WarehouseUtil.PURCHASED);
        order.setStartDate(new Date());
        return order;
    }

    public static Order getOrderObj(Customer customer,Book book) {
        Order order = new Order();
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);
        order.setCustomerId(customer.getTcId());
        order.setOrderPrice(13);
        order.setStatus(WarehouseUtil.PURCHASED);
        order.setStartDate(new Date());
        order.setBookList(bookList);
        return order;
    }

}