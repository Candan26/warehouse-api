package com.readingisgood.warehouseapi.controller;


import com.readingisgood.warehouseapi.model.WarehouseResponse;
import com.readingisgood.warehouseapi.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@Api(value = "Controller block for statistics objects")
@Slf4j
public class StatisticsController {

    private final StatisticsService statisticsService;

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/totalOrderCount")
    @ApiOperation(value = "Get total order without date intervals")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns order statistics list"),
            @ApiResponse(code = 204, message = "If no statistics found on  based on order in stock"),
            @ApiResponse(code = 500, message = "If statistic list get exception.")})
    public ResponseEntity<?> getTotalOrder() {
        try {
            WarehouseResponse response = statisticsService.totalOrderCount();
            if(response.getError()!=null){
                return new ResponseEntity<>(response, response.getError().getStatus());
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception on ", ex);
            return new ResponseEntity<>("Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @CrossOrigin(origins = "*")
    @GetMapping(value = "/totalOrderCountByDate")
    @ApiOperation(value = "Get total order by start stop date intervals")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns order statistics list based on start stop date"),
            @ApiResponse(code = 204, message = "If no statistics found on  based on order in stock"),
            @ApiResponse(code = 500, message = "If statistic list get exception.")})
    public ResponseEntity<?> getTotalOrderByDateInterval(@RequestParam(value = "dateBegin", defaultValue = "") String dateBegin,
                                                         @RequestParam(value = "dateBegin", defaultValue = "") String dateEnd) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            WarehouseResponse response = statisticsService.queryCustomerOrders(format.parse(dateBegin), format.parse(dateEnd));
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
