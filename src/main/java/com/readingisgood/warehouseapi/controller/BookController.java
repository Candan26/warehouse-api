package com.readingisgood.warehouseapi.controller;

import com.readingisgood.warehouseapi.entity.Book;
import com.readingisgood.warehouseapi.entity.Stock;
import com.readingisgood.warehouseapi.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@Api(value = "Controller block for book objects")
public class BookController {

    private final BookService bookService;

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/add")
    @ApiOperation(value = "Insert new book to entity and update stock")
    public ResponseEntity<?> addBook(@RequestBody Book request) {
        try {
            return new ResponseEntity<>(bookService.addBook(request), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/update")
    @ApiOperation(value = "Update stock directly by stock object")
    public ResponseEntity<?> updateStock(@RequestBody Stock request) {
        try {
            return new ResponseEntity<>(bookService.updateBook(request), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
