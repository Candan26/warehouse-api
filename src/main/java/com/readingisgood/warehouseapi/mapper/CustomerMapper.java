package com.readingisgood.warehouseapi.mapper;

import com.readingisgood.warehouseapi.dto.CustomerOrderDto;
import com.readingisgood.warehouseapi.dto.StatisticsByDateDto;
import com.readingisgood.warehouseapi.entity.Customer;
import com.readingisgood.warehouseapi.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;


public interface CustomerMapper {
    Page<CustomerOrderDto> customerSearchEntityToDto(final Customer customer, Page<Order> orders) throws Exception;
    List<StatisticsByDateDto> fromAllOrderMonthlyStatisticsToDto( List<Order> ordersList) throws Exception;
}
