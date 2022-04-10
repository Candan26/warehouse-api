package com.readingisgood.warehouseapi.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Error {
    private HttpStatus status;
    private  String message;
    private  String detail;
    private  int httpStatusCode ;


    public Error(HttpStatus status, String message, Throwable ex){
        this.status = status;
        this.httpStatusCode = status.value();
        this.message= message;
        this.detail = ex.getMessage();
    }

    public Error(HttpStatus status, String message){
        this.status = status;
        this.httpStatusCode = status.value();
        this.message= message;
    }

}
