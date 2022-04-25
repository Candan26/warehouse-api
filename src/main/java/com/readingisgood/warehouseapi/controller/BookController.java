package com.readingisgood.warehouseapi.controller;

import com.readingisgood.warehouseapi.entity.Book;
import com.readingisgood.warehouseapi.entity.Stock;
import com.readingisgood.warehouseapi.model.WarehouseResponse;
import com.readingisgood.warehouseapi.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@Api(value = "Controller block for book objects")
@Slf4j
public class BookController {

    private final BookService bookService;

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/add")
    @ApiOperation(value = "Insert new book to entity and update stock")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book saved on book collection, and new book added on stock."),
            @ApiResponse(code = 400, message = "If Book obj null or book name is null."),
            @ApiResponse(code = 500, message = "If book service get exception.")})
    public ResponseEntity<?> addBook(@RequestBody Book request) {
        try {
            WarehouseResponse response = bookService.addBook(request);
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
    @PostMapping(value = "/update")
    @ApiOperation(value = "Update stock directly by stock object")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Stock obj updated on collection."),
            @ApiResponse(code = 400, message = "If Stock obj null."),
            @ApiResponse(code = 404, message = "If Stock is not in database."),
            @ApiResponse(code = 500, message = "If Stock service get exception.")})
    public ResponseEntity<?> updateStock(@RequestBody Stock request) {
        try {
            WarehouseResponse response = bookService.updateBook(request);
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
