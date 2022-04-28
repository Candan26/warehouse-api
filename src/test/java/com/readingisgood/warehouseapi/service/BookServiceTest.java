package com.readingisgood.warehouseapi.service;

import com.readingisgood.warehouseapi.dto.BookDto;
import com.readingisgood.warehouseapi.dto.StockDto;
import com.readingisgood.warehouseapi.entity.Book;
import com.readingisgood.warehouseapi.entity.Stock;
import com.readingisgood.warehouseapi.mapper.CustomerMapper;
import com.readingisgood.warehouseapi.mapper.impl.CustomerMapperImpl;
import com.readingisgood.warehouseapi.model.WarehouseResponse;
import com.readingisgood.warehouseapi.repository.BookRepository;
import com.readingisgood.warehouseapi.repository.StockRepository;
import com.readingisgood.warehouseapi.service.impl.BookServiceImpl;
import com.readingisgood.warehouseapi.util.WarehouseUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class BookServiceTest {

    private BookService bookService;

    private CustomerMapper customerMapper;

    private StockRepository stockRepository;

    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository = Mockito.mock(BookRepository.class);
        stockRepository = Mockito.mock(StockRepository.class);
        customerMapper = Mockito.mock(CustomerMapperImpl.class);
        bookService = new BookServiceImpl(bookRepository, stockRepository, customerMapper);
    }

    @Test
    public void whenAddBookCalledWithValidRequest_itShouldReturnValidDto() {
        Book book;
        BookDto bookDto;
        book = getBook(WarehouseUtil.VALID);
        bookDto = getBookDto(book);
        Stock stock = getStock(book);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(stockRepository.findByBookName(book.getName())).thenReturn(stock);
        Mockito.when(stockRepository.save(stock)).thenReturn(stock);
        Mockito.when(customerMapper.bookToDto(book)).thenReturn(bookDto);
        WarehouseResponse b = bookService.addBook(book);
        assertEquals(b.getData(), bookDto);
    }

    public static StockDto getStockDto(Book book) {
        StockDto dto = new StockDto();
        Stock stock = getStock(book);
        dto.setTotalPrice(stock.getTotalPrice());
        dto.setTotalQuantity(stock.getTotalQuantity());
        dto.setBookName(stock.getBookName());
        return dto;
    }

    public static Stock getStock(Book book, String status) {
        Stock stock = getStock(book);
        stock.setId(status.equals(WarehouseUtil.VALID) ? "1231415" : null);
        return stock;
    }

    public static Stock getStock(Book book) {
        Stock stock = new Stock();
        stock.setTotalPrice(book.getPrice());
        stock.setBookName(book.getName());
        stock.setTotalQuantity(1);
        return stock;
    }

    public static BookDto getBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setAuthor(book.getAuthor());
        bookDto.setName(book.getName());
        bookDto.setPrice(book.getPrice());

        return bookDto;
    }

    public static Book getBook(String status) {
        Book book = new Book();
        book.setName(status.equals(WarehouseUtil.VALID) ? "testBook" : null);
        book.setAuthor("testAuthor");
        book.setId("12345689021");
        return book;
    }

}