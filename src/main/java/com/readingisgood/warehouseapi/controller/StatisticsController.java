package com.readingisgood.warehouseapi.controller;


import com.readingisgood.warehouseapi.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@Api(value = "Controller block for statistics objects")
@Slf4j
public class StatisticsController {

    private final StatisticsService statisticsService;

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/totalOrderCount/")
    @ApiOperation(value = "Get total order without date intervals")
    public ResponseEntity<?> getTotalOrder() {
        try {
            return new ResponseEntity<>(statisticsService.totalOrderCount(), HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception on ", ex);
            return new ResponseEntity<>("Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @CrossOrigin(origins = "*")
    @GetMapping(value = "/totalOrderCount/{dateBegin}/{dateEnd}")
    @ApiOperation(value = "Get total order by start stop date intervals")
    public ResponseEntity<?> getTotalOrderByDateInterval(@PathVariable Date dateBegin, @PathVariable Date dateEnd) {
        try {
            return new ResponseEntity<>(statisticsService.queryCustomerOrders(dateBegin, dateEnd), HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception on ", ex);
            return new ResponseEntity<>("Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
