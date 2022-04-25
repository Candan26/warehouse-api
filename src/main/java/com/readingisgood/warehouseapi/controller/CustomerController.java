package com.readingisgood.warehouseapi.controller;

import com.readingisgood.warehouseapi.dto.CustomerOrderDto;
import com.readingisgood.warehouseapi.entity.Customer;
import com.readingisgood.warehouseapi.model.WarehouseResponse;
import com.readingisgood.warehouseapi.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "New customer added on customer collection."),
            @ApiResponse(code = 400, message = "If either of customer name tc or surname is empty or null"),
            @ApiResponse(code = 500, message = "If customer service get exception.")})
    public ResponseEntity<?> addCustomer(@RequestBody Customer request) {
        try {
            WarehouseResponse response = customerService.addCustomer(request);
            if (response.getError() != null) {
                return new ResponseEntity<>(response, response.getError().getStatus());
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception on ", ex);
            return new ResponseEntity<>("Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/getOrderList/{page}/{size}/{name}/{surname}")
    @ApiOperation(value = "Query customer order by name surname and pagination")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns customer list in pagination."),
            @ApiResponse(code = 204, message = "If no content found on customer or order by name, surname."),
            @ApiResponse(code = 500, message = "If customer service get exception.")})
    public ResponseEntity<?> queryCustomerOrderByNameAndSurname(@PathVariable int page, @PathVariable int size, @PathVariable String name, @PathVariable String surname) {
        try {
            WarehouseResponse response = customerService.queryCustomerOrders(name, surname, page, size);
            if (response.getError() != null) {
                return new ResponseEntity<>(response, response.getError().getStatus());
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception on ", ex);
            return new ResponseEntity<>("Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/getOrderList/{page}/{size}/{tc}")
    @ApiOperation(value = "Query customer order by tc id and pagination")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns customer list in pagination."),
            @ApiResponse(code = 204, message = "If no content found on customer or order by tc."),
            @ApiResponse(code = 500, message = "If customer service get exception.")})
    public ResponseEntity<?> queryCustomerByTc(@PathVariable int page, @PathVariable int size, @PathVariable String tc) {
        try {
            WarehouseResponse response = customerService.queryCustomerOrders(tc, page, size);
            if (response.getError() != null) {
                return new ResponseEntity<>(response, response.getError().getStatus());
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception on ", ex);
            return new ResponseEntity<>("Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
