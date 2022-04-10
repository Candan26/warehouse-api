package com.readingisgood.warehouseapi.service.impl;

import com.readingisgood.warehouseapi.entity.Book;
import com.readingisgood.warehouseapi.entity.Stock;
import com.readingisgood.warehouseapi.model.Error;
import com.readingisgood.warehouseapi.model.WarehouseResponse;
import com.readingisgood.warehouseapi.repository.BookRepository;
import com.readingisgood.warehouseapi.repository.StockRepository;
import com.readingisgood.warehouseapi.service.BookService;
import com.readingisgood.warehouseapi.util.WarehouseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private static final String ERROR_WRONG_BOOK_DATA = "Please write book name";

    private static final String ERROR_STOCK_NULL_OBJECT = "Stock object cannot be null";

    private static final String ERROR_OBJECT_NOT_IN_STOCK = "Given book id not exist in stock";

    private final BookRepository bookRepository;

    private final StockRepository stockRepository;

    @Override
    public WarehouseResponse addBook(Book book) {
        try {
            if (book == null || book.getName() == null) {
                log.error("book object or name is null");
                return new WarehouseResponse(WarehouseUtil.FAILED, "", new Error(HttpStatus.BAD_REQUEST, ERROR_WRONG_BOOK_DATA));
            }
            book = bookRepository.save(book);
            addBookToStock(book);

            return new WarehouseResponse(WarehouseUtil.SUCCEED, book, null);
        } catch (Exception ex) {
            log.error("Exception on ", ex);
            return new WarehouseResponse(WarehouseUtil.FAILED, "", new Error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex));
        }
    }

    @Override
    public WarehouseResponse updateBook(Stock stock) {
        try {
            if (stock == null) {
                log.error("Stock object is null");
                return new WarehouseResponse(WarehouseUtil.FAILED, "", new Error(HttpStatus.BAD_REQUEST, ERROR_STOCK_NULL_OBJECT));
            }
            Stock stockFromDb = stockRepository.findById(stock.getId()==null ?"": stock.getId()).get();
            cloneStockObj(stock, stockFromDb);
            return new WarehouseResponse(WarehouseUtil.SUCCEED, stockRepository.save(stockFromDb), null);
        } catch (Exception ex) {
            log.error("Exception on" , ex);
            return new WarehouseResponse(WarehouseUtil.FAILED, "", new Error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex));
        }
    }

    private void cloneStockObj(Stock stock, Stock stockFromDb) {
        stockFromDb.setBookName(stock.getBookName());
        stockFromDb.setTotalPrice(stock.getTotalPrice());
        stockFromDb.setTotalQuantity(stock.getTotalQuantity());
    }

    private void addBookToStock(Book book) {

        Stock stock = stockRepository.findByBookName(book.getName());
        if (stock == null) {
            stock = new Stock();
            stock.setTotalPrice(book.getPrice());
            stock.setBookName(book.getName());
            stock.setTotalQuantity(1);
            log.debug("the book: "+book.getName() + "doesnt exist on stock creating new");
        } else {
            stock.setTotalQuantity(stock.getTotalQuantity() + 1);
            stock.setTotalPrice(stock.getTotalPrice() + book.getPrice());
            log.debug("the book: "+book.getName() + "exist on stock updating fields");
        }
        stockRepository.save(stock);
    }

}
