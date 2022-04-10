package com.readingisgood.warehouseapi.dto;

import lombok.Data;

@Data
public class CustomerDto {
    private String name;
    private String surname;
    private String email;
    private int age;
}
