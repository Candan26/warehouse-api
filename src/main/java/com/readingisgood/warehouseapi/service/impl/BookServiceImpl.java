package com.readingisgood.warehouseapi.service.impl;

import com.readingisgood.warehouseapi.entity.Book;
import com.readingisgood.warehouseapi.entity.Stock;
import com.readingisgood.warehouseapi.model.Error;
import com.readingisgood.warehouseapi.model.WarehouseResponse;
import com.readingisgood.warehouseapi.repository.BookRepository;
import com.readingisgood.warehouseapi.repository.StockRepository;
import com.readingisgood.warehouseapi.service.BookService;
import com.readingisgood.warehouseapi.util.WarehouseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private static final String ERROR_WRONG_BOOK_DATA = "Please write book name";

    private static final String ERROR_STOCK_NULL_OBJECT = "Stock object cannot be null";

    @Autowired
    BookRepository bookRepository;

    @Autowired
    StockRepository stockRepository;

    @Override
    public WarehouseResponse addBook(Book book) {
        try {
            if (book == null || book.getName() ==null ) {
                return new WarehouseResponse(WarehouseUtil.FAILED, "", new Error(HttpStatus.BAD_REQUEST, ERROR_WRONG_BOOK_DATA));
            }
            book = bookRepository.save(book);
            addBookToStock(book);

            return new WarehouseResponse(WarehouseUtil.SUCCEED, book, null);
        } catch (Exception ex) {
            return new WarehouseResponse(WarehouseUtil.FAILED, "", new Error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex));
        }
    }

    @Override
    public WarehouseResponse updateBook(Stock stock) {
        try {
            if (stock == null) {
                return new WarehouseResponse(WarehouseUtil.FAILED, "", new Error(HttpStatus.BAD_REQUEST, ERROR_STOCK_NULL_OBJECT));
            }
            return new WarehouseResponse(WarehouseUtil.SUCCEED,  stockRepository.save(stock), null);
        } catch (Exception ex) {
            return new WarehouseResponse(WarehouseUtil.FAILED, "", new Error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex));
        }
    }

    private void addBookToStock(Book book) {

        Stock stock = stockRepository.findByBookName(book.getName());
        if(stock ==null ){
            stock = new Stock();
            stock.setTotalPrice(book.getPrice());
            stock.setBookName(book.getName());
            stock.setTotalQuantity(1);
        }else {
            stock.setTotalQuantity(stock.getTotalQuantity()+1);
            stock.setTotalPrice(stock.getTotalPrice()+book.getPrice());
        }
        stockRepository.save(stock);
    }

}
