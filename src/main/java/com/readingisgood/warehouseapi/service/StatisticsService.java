package com.readingisgood.warehouseapi.service;

import com.readingisgood.warehouseapi.dto.StatisticsByDateDto;

import java.util.Date;
import java.util.List;

public interface StatisticsService {
    List<StatisticsByDateDto> totalOrderCount() throws Exception;
    List<StatisticsByDateDto> queryCustomerOrders(Date dateBegin, Date dateEnd) throws Exception;
}
