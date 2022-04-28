package com.readingisgood.warehouseapi.service;

import com.readingisgood.warehouseapi.dto.StatisticsByDateDto;
import com.readingisgood.warehouseapi.entity.Customer;
import com.readingisgood.warehouseapi.entity.Order;
import com.readingisgood.warehouseapi.mapper.CustomerMapper;
import com.readingisgood.warehouseapi.model.WarehouseResponse;
import com.readingisgood.warehouseapi.repository.OrderRepository;
import com.readingisgood.warehouseapi.service.impl.StatisticsServiceImpl;
import com.readingisgood.warehouseapi.util.WarehouseUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class StatisticsServiceTest {

    private StatisticsService statisticsService;
    private OrderRepository orderRepository;
    private CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        orderRepository = Mockito.mock(OrderRepository.class);
        customerMapper = Mockito.mock(CustomerMapper.class);
        statisticsService = new StatisticsServiceImpl(orderRepository, customerMapper);
    }

    @Test
    public void whenQueryCustomerOrderCalledWithValidRequest_itShouldReturnValidDto() throws Exception {
        Date dateBegin = new Date();
        Date dateEnd = new Date();
        List<Order> orderList = new ArrayList<>();
        List<StatisticsByDateDto> statisticsByDateDtoList = new ArrayList<>();

        Customer customer = CustomerServiceTest.getCustomer(WarehouseUtil.VALID);
        Order order = OrderServiceTest.getOrderObj(customer);
        orderList.add(order);
        StatisticsByDateDto statisticsByDateDto =  getStatisticsByDateDto();
        statisticsByDateDtoList.add(statisticsByDateDto);
        Mockito.when(orderRepository.findByStartDateBetween(dateBegin, dateEnd)).thenReturn(orderList);
        Mockito.when( customerMapper.fromAllOrderMonthlyStatisticsToDto(orderList)).thenReturn(statisticsByDateDtoList);
        WarehouseResponse warehouseResponse = statisticsService.queryCustomerOrders(dateBegin,dateEnd);
        assertEquals(warehouseResponse.getData(), statisticsByDateDtoList);
        statisticsService.queryCustomerOrders(dateBegin, dateEnd);
    }

    @Test
    public void whenQueryCustomerOrderCalledWithInValidRequest_itShouldReturnError() throws Exception {
        Date dateBegin = new Date();
        Date dateEnd = new Date();
        List<Order> orderList = new ArrayList<>();
        List<StatisticsByDateDto> statisticsByDateDtoList = new ArrayList<>();

        Customer customer = CustomerServiceTest.getCustomer(WarehouseUtil.VALID);
        Order order = OrderServiceTest.getOrderObj(customer);
        orderList.add(order);
        StatisticsByDateDto statisticsByDateDto =  getStatisticsByDateDto();
        statisticsByDateDtoList.add(statisticsByDateDto);
        Mockito.when(orderRepository.findByStartDateBetween(dateBegin, dateEnd)).thenReturn(orderList);
        Mockito.when( customerMapper.fromAllOrderMonthlyStatisticsToDto(orderList)).thenReturn(null);
        WarehouseResponse warehouseResponse = statisticsService.queryCustomerOrders(dateBegin,dateEnd);
        assertNotEquals(warehouseResponse.getData(), statisticsByDateDtoList);
        log.error("Error message " + warehouseResponse.getError().getMessage());
    }

    @Test
    public void whenTotalOrderCountCalledWithValidRequest_itShouldReturnValidDto() throws Exception {
        List<Order> orderList = new ArrayList<>();
        List<StatisticsByDateDto> statisticsByDateDtoList = new ArrayList<>();

        Customer customer = CustomerServiceTest.getCustomer(WarehouseUtil.VALID);
        Order order = OrderServiceTest.getOrderObj(customer);
        orderList.add(order);
        StatisticsByDateDto statisticsByDateDto =  getStatisticsByDateDto();
        statisticsByDateDtoList.add(statisticsByDateDto);
        Mockito.when(orderRepository.findAll()).thenReturn(orderList);
        Mockito.when( customerMapper.fromAllOrderMonthlyStatisticsToDto(orderList)).thenReturn(statisticsByDateDtoList);
        WarehouseResponse warehouseResponse = statisticsService.totalOrderCount();
        assertEquals(warehouseResponse.getData(), statisticsByDateDtoList);
    }

    @Test
    public void whenTotalOrderCountCalledWithInValidRequest_itShouldReturnError() throws Exception {
        List<Order> orderList = new ArrayList<>();
        List<StatisticsByDateDto> statisticsByDateDtoList = new ArrayList<>();

        Customer customer = CustomerServiceTest.getCustomer(WarehouseUtil.VALID);
        Order order = OrderServiceTest.getOrderObj(customer);
        orderList.add(order);
        StatisticsByDateDto statisticsByDateDto =  getStatisticsByDateDto();
        statisticsByDateDtoList.add(statisticsByDateDto);
        Mockito.when(orderRepository.findAll()).thenReturn(orderList);
        Mockito.when( customerMapper.fromAllOrderMonthlyStatisticsToDto(orderList)).thenReturn(null);
        WarehouseResponse warehouseResponse = statisticsService.totalOrderCount();
        assertNotEquals(warehouseResponse.getData(), statisticsByDateDtoList);
        log.error("Error message " + warehouseResponse.getError().getMessage());
    }

    public static StatisticsByDateDto getStatisticsByDateDto() {
        StatisticsByDateDto dto = new StatisticsByDateDto();
        dto.setDate(String.valueOf(new Date()));
        dto.setTotalOrderCount(1);
        dto.setTotalBookCount(1);
        dto.setTotalPurchasedAmount(BigDecimal.valueOf(1d));
        return  dto;
    }
}