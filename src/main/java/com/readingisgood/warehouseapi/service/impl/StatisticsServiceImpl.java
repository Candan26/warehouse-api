package com.readingisgood.warehouseapi.service.impl;

import com.readingisgood.warehouseapi.entity.Order;
import com.readingisgood.warehouseapi.mapper.CustomerMapper;
import com.readingisgood.warehouseapi.model.Error;
import com.readingisgood.warehouseapi.model.WarehouseResponse;
import com.readingisgood.warehouseapi.repository.OrderRepository;
import com.readingisgood.warehouseapi.service.StatisticsService;
import com.readingisgood.warehouseapi.util.WarehouseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private static final String ERROR_NO_CONTENT_STATISTICS = "Empty order List ";
    private final OrderRepository orderRepository;
    private final CustomerMapper customerMapper;


    @Override
    public WarehouseResponse totalOrderCount() throws Exception {
        List<Order> orderList = orderRepository.findAll();
        return getWarehouseResponse(orderList);
    }

    @Override
    public WarehouseResponse  queryCustomerOrders(Date dateBegin, Date dateEnd) throws Exception {
        List<Order> orderList = orderRepository.findByStartDateBetween(dateBegin, dateEnd);
        return getWarehouseResponse(orderList);
    }

    private WarehouseResponse getWarehouseResponse(List<Order> orderList) throws Exception {
        var val = customerMapper.fromAllOrderMonthlyStatisticsToDto(orderList);
        if(val== null || val.isEmpty()){
            log.info("Empty order List ");
            return new WarehouseResponse(WarehouseUtil.FAILED, "", new Error(HttpStatus.NO_CONTENT, ERROR_NO_CONTENT_STATISTICS));
        }
        return new WarehouseResponse(WarehouseUtil.SUCCEED, val, null);
    }
}
