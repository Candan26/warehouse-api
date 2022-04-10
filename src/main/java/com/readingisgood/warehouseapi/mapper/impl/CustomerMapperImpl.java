package com.readingisgood.warehouseapi.mapper.impl;

import com.readingisgood.warehouseapi.dto.CustomerOrderDto;
import com.readingisgood.warehouseapi.dto.StatisticsByDateDto;
import com.readingisgood.warehouseapi.entity.Customer;
import com.readingisgood.warehouseapi.entity.Order;
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

        if(ordersList== null || ordersList.isEmpty()){
            log.info("Empty order List returning empty StatisticsByDateDto object");
            return statistics;
        }
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

    private int  updateStatisticList(List<StatisticsByDateDto> statistics, SimpleDateFormat sdf, Order o) {
        int index = 0;
        ListIterator<StatisticsByDateDto> iterator = statistics.listIterator();
        while (iterator.hasNext()) {
            StatisticsByDateDto next = iterator.next();
            if (next.getDate().equals(sdf.format(o.getStartDate()))) {
                next.setTotalPurchasedAmount(BigDecimal.valueOf(next.getTotalPurchasedAmount().doubleValue()+ o.getBookList().size()* o.getBookList().get(0).getPrice()));
                next.setTotalOrderCount(next.getTotalOrderCount()+1);
                next.setTotalBookCount(next.getTotalBookCount()+o.getBookList().size());
                statistics.set(index,next);
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
