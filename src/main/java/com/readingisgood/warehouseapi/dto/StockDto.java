package com.readingisgood.warehouseapi.dto;

import lombok.Data;

@Data
public class StockDto {
    private String bookName;
    private int totalQuantity;
    private Double totalPrice;
}
