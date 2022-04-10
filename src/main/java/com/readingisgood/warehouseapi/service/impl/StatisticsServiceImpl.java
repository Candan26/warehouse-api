package com.readingisgood.warehouseapi.service.impl;

import com.readingisgood.warehouseapi.dto.StatisticsByDateDto;
import com.readingisgood.warehouseapi.entity.Order;
import com.readingisgood.warehouseapi.mapper.CustomerMapper;
import com.readingisgood.warehouseapi.model.WarehouseResponse;
import com.readingisgood.warehouseapi.repository.OrderRepository;
import com.readingisgood.warehouseapi.service.StatisticsService;
import com.readingisgood.warehouseapi.util.WarehouseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final OrderRepository orderRepository;
    private final CustomerMapper customerMapper;


    @Override
    public WarehouseResponse totalOrderCount() throws Exception {
        List<Order> orderList = orderRepository.findAll();
        return new WarehouseResponse(WarehouseUtil.SUCCEED, customerMapper.fromAllOrderMonthlyStatisticsToDto(orderList), null);
    }

    @Override
    public WarehouseResponse  queryCustomerOrders(Date dateBegin, Date dateEnd) throws Exception {
        List<Order> orderList = orderRepository.findByStartDateBetween(dateBegin, dateEnd);
        return new WarehouseResponse(WarehouseUtil.SUCCEED, customerMapper.fromAllOrderMonthlyStatisticsToDto(orderList), null);
    }
}
