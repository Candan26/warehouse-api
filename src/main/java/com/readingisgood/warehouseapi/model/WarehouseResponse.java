package com.readingisgood.warehouseapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WarehouseResponse {
    private String returnCode;
    @JsonFormat(shape =  JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timeStamp;
    private Object data;
    private  Error error;
    public WarehouseResponse() {
        this.timeStamp = LocalDateTime.now();
    }
    public WarehouseResponse( String returnCode, Object data, Error error){
        this();
        this.returnCode = returnCode;
        this.data = data;
        this.error = error;
    }
    public WarehouseResponse( String returnCode,LocalDateTime timeStamp, Object data, Error error){
        this.timeStamp = timeStamp;
        this.returnCode = returnCode;
        this.data = data;
        this.error = error;
    }
}
