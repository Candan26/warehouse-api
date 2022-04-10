package com.readingisgood.warehouseapi.controller;


import com.readingisgood.warehouseapi.service.StatisticsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/statistics")
@Api(value = "Controller block for statistics objects")
public class StatisticsController {


    @Autowired
    StatisticsService statisticsService;

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/totalOrderCount/")
    public ResponseEntity<?> getTotalOrder() {
        try {
            return new ResponseEntity<>( statisticsService.totalOrderCount(), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>( "Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @CrossOrigin(origins = "*")
    @GetMapping(value = "/totalOrderCount/{dateBegin}/{dateEnd}")
    public ResponseEntity<?> getTotalOrderByDateInterval(@PathVariable Date dateBegin, @PathVariable Date dateEnd ) {
        try {
            return new ResponseEntity<>( statisticsService.queryCustomerOrders(dateBegin,dateEnd), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>( "Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
