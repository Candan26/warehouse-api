package com.readingisgood.warehouseapi.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@ToString
public class Stock {
    @Id
    @ApiModelProperty(notes = "The database generated order ID")
    private String id;
    @ApiModelProperty(notes = "The book name which stored in stock",required = true)
    private String bookName;
    @ApiModelProperty(notes = "The total quantity of books which stored in stock")
    private int totalQuantity;
    @ApiModelProperty(notes = "The total price of books which stored in stock")
    private Double totalPrice;
}
