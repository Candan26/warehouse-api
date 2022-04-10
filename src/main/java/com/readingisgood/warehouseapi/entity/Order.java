package com.readingisgood.warehouseapi.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Document
@ToString
public class Order {
    @Id
    private String id;
    private int orderNumber;
    private String status;
    private Date startDate;
    private double orderPrice;
    private String customerId;
    private List<Book> bookList;
}
