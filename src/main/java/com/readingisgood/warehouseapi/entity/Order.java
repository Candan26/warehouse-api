package com.readingisgood.warehouseapi.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document
@ToString
public class Order {
    @Id
    @ApiModelProperty(notes = "The database generated order ID")
    private String id;
    @ApiModelProperty(notes = "The number of order",required = true)
    private int orderNumber;
    @ApiModelProperty(notes = "The status of the order")
    private String status;
    @ApiModelProperty(notes = "The start date of the order")
    private Date startDate;
    @ApiModelProperty(notes = "The price of the order which calculated based on book list in entity")
    private double orderPrice;
    @ApiModelProperty(notes = "Each order must match with a customer", required = true)
    private String customerId;
    @ApiModelProperty(notes = "The book list of the order")
    private List<Book> bookList;
}
