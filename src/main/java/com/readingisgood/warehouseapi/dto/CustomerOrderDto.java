package com.readingisgood.warehouseapi.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerOrderDto {
    private  String orderId;
    private  String orderStatus;
    private Date orderDate;
    private  String customerName;
    private  String customerTc;
    private  String CustomerSurname;
}
