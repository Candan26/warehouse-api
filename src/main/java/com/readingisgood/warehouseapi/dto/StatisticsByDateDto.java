package com.readingisgood.warehouseapi.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatisticsByDateDto {
    private String date;
    private int totalOrderCount;
    private int totalBookCount;
    private BigDecimal totalPurchasedAmount;
}
