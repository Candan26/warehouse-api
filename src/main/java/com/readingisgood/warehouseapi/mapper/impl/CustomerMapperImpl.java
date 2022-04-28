package com.readingisgood.warehouseapi.mapper.impl;

import com.readingisgood.warehouseapi.dto.*;
import com.readingisgood.warehouseapi.entity.Book;
import com.readingisgood.warehouseapi.entity.Customer;
import com.readingisgood.warehouseapi.entity.Order;
import com.readingisgood.warehouseapi.entity.Stock;
import com.readingisgood.warehouseapi.mapper.CustomerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

@Component
@Slf4j
public class CustomerMapperImpl implements CustomerMapper {
    @Override
    public Page<CustomerOrderDto> customerSearchEntityToDto(final Customer customer, Page<Order> orders) {
        return orders.map(new Function<>() {
            @Override
            public CustomerOrderDto apply(Order order) {
                return composeOrderAndCustomer(customer, order);
            }
            private CustomerOrderDto composeOrderAndCustomer(Customer customer, Order order) {
                CustomerOrderDto dto = new CustomerOrderDto();
                if (customer == null || order == null) {
                    log.info("customer obj null returning empty dto");
                    return dto;
                }
                dto.setCustomerTc(customer.getTcId());
                dto.setCustomerName(customer.getName());
                dto.setCustomerSurname(customer.getSurname());
                dto.setOrderId(order.getId());
                dto.setOrderStatus(order.getStatus());
                dto.setOrderDate(order.getStartDate());
                return dto;
            }
        });
    }

    @Override
    public List<StatisticsByDateDto> fromAllOrderMonthlyStatisticsToDto(List<Order> ordersList) {
        Map<String , StatisticsByDateDto> map = new HashMap<>();
        List<StatisticsByDateDto> statistics = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
        for(Order o : ordersList){
            if (!map.containsKey(sdf.format(o.getStartDate()))) {
                addNewStatistic(statistics, sdf, o);
                map.put(sdf.format(o.getStartDate()),statistics.get(0));
            }else{
                map.put(sdf.format(o.getStartDate()),statistics.get(updateStatisticList(statistics,sdf,o)));
            }
        }
        return statistics;
    }

    @Override
    public CustomerDto customerToDto(Customer customer) {
        CustomerDto dto  = new CustomerDto();
        dto.setName(customer.getName());
        dto.setSurname(customer.getSurname());
        dto.setAge(customer.getAge());
        dto.setEmail(customer.getEmail());
        return dto;
    }

    @Override
    public BookDto bookToDto(Book book) {
        BookDto dto = new BookDto();
        dto.setAuthor(book.getAuthor());
        dto.setName(book.getName());
        dto.setPrice(book.getPrice());
        return dto;
    }

    @Override
    public OrderDto orderToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setCustomerId(order.getCustomerId());
        dto.setOrderPrice(dto.getOrderPrice());
        dto.setOrderNumber(dto.getOrderNumber());
        dto.setBookList(dto.getBookList());
        dto.setStatus(dto.getStatus());
        dto.setStartDate(dto.getStartDate());
        return dto;
    }


    private int  updateStatisticList(List<StatisticsByDateDto> statistics, SimpleDateFormat sdf, Order o) {
        int index = 0;
        for (StatisticsByDateDto next : statistics) {
            if (next.getDate().equals(sdf.format(o.getStartDate()))) {
                next.setTotalPurchasedAmount(BigDecimal.valueOf(next.getTotalPurchasedAmount().doubleValue() + o.getBookList().size() * o.getBookList().get(0).getPrice()));
                next.setTotalOrderCount(next.getTotalOrderCount() + 1);
                next.setTotalBookCount(next.getTotalBookCount() + o.getBookList().size());
                statistics.set(index, next);
                log.debug("Found StatisticsDto object on same date " + sdf.format(o.getStartDate()));
                break;
            }
            index++;
        }
        return  index;
    }

    private void addNewStatistic(List<StatisticsByDateDto> statistics, SimpleDateFormat sdf, Order o) {
        StatisticsByDateDto sbdd  = new StatisticsByDateDto();
        sbdd.setDate(sdf.format(o.getStartDate()));
        sbdd.setTotalOrderCount(1);
        sbdd.setTotalBookCount(o.getBookList().size());
        sbdd.setTotalPurchasedAmount(BigDecimal.valueOf(o.getBookList().size()* o.getBookList().get(0).getPrice()));
        statistics.add(sbdd);
    }
}
