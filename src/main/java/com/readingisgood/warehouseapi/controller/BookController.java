package com.readingisgood.warehouseapi.controller;

import com.readingisgood.warehouseapi.entity.Book;
import com.readingisgood.warehouseapi.entity.Customer;
import com.readingisgood.warehouseapi.entity.Stock;
import com.readingisgood.warehouseapi.service.BookService;
import com.readingisgood.warehouseapi.service.CustomerService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
@Api(value = "Controller block for book objects")
public class BookController {

    @Autowired
    BookService bookService;

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/add")
    public ResponseEntity<?> addBook(@RequestBody Book request) {
        try {
            return new ResponseEntity<>(bookService.addBook(request), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>( "Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/update")
    public ResponseEntity<?> updateStock(@RequestBody Stock request) {
        try {
            return new ResponseEntity<>(bookService.updateBook(request), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>( "Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
