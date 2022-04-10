package com.readingisgood.warehouseapi.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Stock {
    @Id
    private String id;
    private String bookName;
    private int totalQuantity;
    private Double totalPrice;
}
