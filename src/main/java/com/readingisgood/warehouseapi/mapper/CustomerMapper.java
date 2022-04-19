package com.readingisgood.warehouseapi.mapper;

import com.readingisgood.warehouseapi.dto.*;
import com.readingisgood.warehouseapi.entity.Book;
import com.readingisgood.warehouseapi.entity.Customer;
import com.readingisgood.warehouseapi.entity.Order;
import com.readingisgood.warehouseapi.entity.Stock;
import org.springframework.data.domain.Page;

import java.util.List;


public interface CustomerMapper {
    Page<CustomerOrderDto> customerSearchEntityToDto(final Customer customer, Page<Order> orders) throws Exception;
    List<StatisticsByDateDto> fromAllOrderMonthlyStatisticsToDto( List<Order> ordersList) throws Exception;
    CustomerDto customerToDto(Customer customer);
    BookDto bookToDto(Book book);
    StockDto stockToDto(Stock stock);
    OrderDto orderToDto(Order order);
}
