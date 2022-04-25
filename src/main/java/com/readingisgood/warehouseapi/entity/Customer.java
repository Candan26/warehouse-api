package com.readingisgood.warehouseapi.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@ToString
public class Customer {
    @Id
    @ApiModelProperty(notes = "The database generated customer ID")
    private String id;
    @ApiModelProperty(notes = "The name of the customer", required = true)
    private String name;
    @ApiModelProperty(notes = "The surname of the customer")
    private String surname;
    @ApiModelProperty(notes = "The tc id of the customer")
    private String tcId;
    @ApiModelProperty(notes = "The email of the customer")
    private String email;
    @ApiModelProperty(notes = "The age of the customer")
    private int age;
    @ApiModelProperty(notes = "whether customer has order or not")
    private boolean hasOrder;
}
