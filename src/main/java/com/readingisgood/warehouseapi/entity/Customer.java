package com.readingisgood.warehouseapi.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@ToString
public class Customer {
    @Id
    private String id;
    private String name;
    private String surname;
    private String tcId;
    private String email;
    private int age;
    private boolean hasOrder;
}
