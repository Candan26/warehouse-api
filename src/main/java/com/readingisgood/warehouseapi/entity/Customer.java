package com.readingisgood.warehouseapi.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Customer {
    @Id
    private String id;
    private String name;
    private String surname;
    private String tcId;
    private int age;
    private boolean hasOrder;
}
