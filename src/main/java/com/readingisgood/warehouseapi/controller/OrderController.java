package com.readingisgood.warehouseapi.controller;

import com.readingisgood.warehouseapi.entity.Order;
import com.readingisgood.warehouseapi.model.WarehouseResponse;
import com.readingisgood.warehouseapi.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Api(value = "Controller block for order objects")
@Slf4j
public class OrderController {

    private final OrderService orderService;
   
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/getByDateInterval")
    @ApiOperation(value = "Get order list by start stop date interval")
    public ResponseEntity<?> queryOrderByDateInterval(@RequestParam(value = "startDate", defaultValue = "") String startDate,
                                                      @RequestParam(value = "stopDate", defaultValue = "") String stopDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return new ResponseEntity<>(orderService.queryOrdersByDateInterval(format.parse(startDate), format.parse(stopDate)), HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception on ", ex);
            return new ResponseEntity<>("Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/getById/{id}")
    @ApiOperation(value = "Get order by order ID")
    public ResponseEntity<?> queryOrderById(@PathVariable String id) {
        try {
            return new ResponseEntity<>(orderService.queryOrdersById(id), HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception on ", ex);
            return new ResponseEntity<>("Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @CrossOrigin(origins = "*")
    @PostMapping(value = "/add")
    @ApiOperation(value = "Add new order on Order entity this endpoint updates also the stock values")
    public ResponseEntity<?> addNewOrder(@RequestBody Order request) {
        try {
            WarehouseResponse response = orderService.addNewOrder(request);
            if(response.getError()!=null){
                return new ResponseEntity<>(response, response.getError().getStatus());
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception on ", ex);
            return new ResponseEntity<>("Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
