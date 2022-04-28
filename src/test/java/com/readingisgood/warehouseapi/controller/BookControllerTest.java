package com.readingisgood.warehouseapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readingisgood.warehouseapi.entity.Book;
import com.readingisgood.warehouseapi.repository.BookRepository;
import com.readingisgood.warehouseapi.repository.StockRepository;
import com.readingisgood.warehouseapi.util.WarehouseJwtUtil;
import com.readingisgood.warehouseapi.util.WarehouseUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WarehouseJwtUtil jwtUtil;

    private String token;

    @BeforeEach
    void setUp() {
        token = jwtUtil.generateToken("test");
    }

    @AfterAll
    static void afterAll(@Autowired BookRepository bookRepository, @Autowired StockRepository stockRepository) {
        bookRepository.deleteAll();
        stockRepository.deleteAll();
    }

    @BeforeAll
    static void beforeAll(@Autowired BookRepository bookRepository, @Autowired StockRepository stockRepository) {
        bookRepository.deleteAll();
        stockRepository.deleteAll();
    }

    @Test
    void addBook_shouldReturnValidDto() throws Exception {
        Book book = getBook(WarehouseUtil.VALID);
        mockMvc.perform(post("/book/add").
                        contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(book))
                ).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void addBookWithoutName_shouldReturnBadRequest() throws Exception {
        // Try unouthirezed method
        Book book= new Book();
        book.setAuthor("testAuthor");
        book.setPrice(1231);
        String accessToken = token;
        mockMvc.perform(post("/book/add").
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(book))
        ).andExpect(status().isBadRequest());
    }

    public static Book getBook(String status) {
        Book book = new Book();
        book.setName(status.equals(WarehouseUtil.VALID) ? "testBook" : null);
        book.setAuthor("testAuthor");
        book.setId("12345689021");
        return book;
    }
}