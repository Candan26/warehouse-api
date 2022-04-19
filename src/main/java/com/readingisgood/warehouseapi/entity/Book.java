package com.readingisgood.warehouseapi.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@ToString
public class Book {
    @Id
    @ApiModelProperty(notes = "The database generated book ID")
    private String id;
    @ApiModelProperty(notes = "The name of the book", required = true)
    private String name;
    @ApiModelProperty(notes = "The author of the book")
    private String author;
    @ApiModelProperty(notes = "The price of the book")
    private double price;
}
