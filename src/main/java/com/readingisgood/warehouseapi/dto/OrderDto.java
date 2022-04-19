package com.readingisgood.warehouseapi.dto;

import com.readingisgood.warehouseapi.entity.Book;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class OrderDto {
    private int orderNumber;
    private String status;
    private Date startDate;
    private double orderPrice;
    private String customerId;
    private List<Book> bookList;
}
