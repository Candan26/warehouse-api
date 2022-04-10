package com.readingisgood.warehouseapi.controller;

import com.readingisgood.warehouseapi.entity.Customer;
import com.readingisgood.warehouseapi.service.CustomerService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@Api(value = "Controller block for customer objects")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/add")
    public ResponseEntity<?> addCustomer(@RequestBody Customer request) {
        try {
            return new ResponseEntity<>( customerService.addCustomer(request), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>( "Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/getOrderList/{page}/{size}/{name}/{surname}")
    public ResponseEntity<?> queryCustomerByNameAndSurname(@PathVariable int page, @PathVariable int size, @PathVariable String  name, @PathVariable String surname ) {
        try {
            return new ResponseEntity<>( customerService.queryCustomerOrders(name,surname,page,size), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>( "Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/getOrderList/{page}/{size}/{tc}")
    public ResponseEntity<?> queryCustomerByTc(@PathVariable int page, @PathVariable int size, @PathVariable String  tc) {
        try {
            return new ResponseEntity<>( customerService.queryCustomerOrders(tc,page,size), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>( "Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
