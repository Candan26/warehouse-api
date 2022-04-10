package com.readingisgood.warehouseapi.controller;

import com.readingisgood.warehouseapi.entity.Order;
import com.readingisgood.warehouseapi.service.OrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/order")
@Api(value = "Controller block for order objects")
public class OrderController {

    @Autowired
    OrderService orderService;

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/getOrderByDateInterval/{startDate}/{stopDate}")
    public ResponseEntity<?> queryOrderByDateInterval(@PathVariable Date startDate, @PathVariable Date stopDate) {
        try {
            return new ResponseEntity<>( orderService.queryOrdersByDateInterval(startDate,stopDate), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>( "Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/getOrderById/{id}")
    public ResponseEntity<?> queryOrderByDateInterval(@PathVariable String id) {
        try {
            return new ResponseEntity<>( orderService.queryOrdersById(id), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>( "Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @CrossOrigin(origins = "*")
    @PostMapping(value = "/add")
    public ResponseEntity<?> addNewOrder(@RequestBody Order request) {
        try {
            return new ResponseEntity<>(orderService.addNewOrder(request), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>( "Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
