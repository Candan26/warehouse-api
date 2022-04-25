package com.readingisgood.warehouseapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BookDto {
    private String name;
    private String author;
    private double price;
}
