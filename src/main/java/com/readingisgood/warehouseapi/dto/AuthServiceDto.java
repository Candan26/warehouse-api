package com.readingisgood.warehouseapi.dto;

import lombok.Data;

@Data
public class AuthServiceDto {
    private Boolean error;
    private String errorMessage;
    private String token;
    private String subject;
    private long idleTime;

}
