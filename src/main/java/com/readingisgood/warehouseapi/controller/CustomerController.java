package com.readingisgood.warehouseapi.controller;

import com.readingisgood.warehouseapi.entity.Customer;
import com.readingisgood.warehouseapi.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@Api(value = "Controller block for customer objects")
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/add")
    @ApiOperation(value = "Add customer directly by customer object")
    public ResponseEntity<?> addCustomer(@RequestBody Customer request) {
        try {
            return new ResponseEntity<>(customerService.addCustomer(request), HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception on ", ex);
            return new ResponseEntity<>("Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/getOrderList/{page}/{size}/{name}/{surname}")
    @ApiOperation(value = "Query customer order by name surname and pagination")
    public ResponseEntity<?> queryCustomerOrderByNameAndSurname(@PathVariable int page, @PathVariable int size, @PathVariable String name, @PathVariable String surname) {
        try {
            return new ResponseEntity<>(customerService.queryCustomerOrders(name, surname, page, size), HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception on ", ex);
            return new ResponseEntity<>("Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/getOrderList/{page}/{size}/{tc}")
    @ApiOperation(value = "Query customer order by tc id and pagination")
    public ResponseEntity<?> queryCustomerByTc(@PathVariable int page, @PathVariable int size, @PathVariable String tc) {
        try {
            return new ResponseEntity<>(customerService.queryCustomerOrders(tc, page, size), HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception on ", ex);
            return new ResponseEntity<>("Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
