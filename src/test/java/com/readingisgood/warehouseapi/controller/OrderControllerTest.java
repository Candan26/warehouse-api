package com.readingisgood.warehouseapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readingisgood.warehouseapi.entity.Book;
import com.readingisgood.warehouseapi.entity.Customer;
import com.readingisgood.warehouseapi.entity.Order;
import com.readingisgood.warehouseapi.model.WarehouseResponse;
import com.readingisgood.warehouseapi.repository.BookRepository;
import com.readingisgood.warehouseapi.repository.CustomerRepository;
import com.readingisgood.warehouseapi.repository.OrderRepository;
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
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.readingisgood.warehouseapi.controller.BookControllerTest.getBook;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WarehouseJwtUtil jwtUtil;

    @Autowired
    OrderRepository orderRepository;

    private String token;

    @BeforeEach
    void setUp() {
        token = jwtUtil.generateToken("test");
    }

    @AfterAll
    static void afterAll(@Autowired StockRepository stockRepository, @Autowired OrderRepository orderRepository, @Autowired BookRepository bookRepository) {
        stockRepository.deleteAll();
        orderRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @BeforeAll
    static void beforeAll(@Autowired StockRepository stockRepository, @Autowired OrderRepository orderRepository, @Autowired BookRepository bookRepository) {
        stockRepository.deleteAll();
        orderRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @Test
    void addNewOrder_shouldReturnValidDto() throws Exception {
        Book book = getBook(WarehouseUtil.VALID);
        mockMvc.perform(post("/book/add").contentType(MediaType.APPLICATION_JSON).
                header("Authorization", "Bearer " + token).
                content(objectMapper.writeValueAsString(book))).
                andExpect(status().isOk()).
                andDo(print()).
                andReturn();

        List<Book> bl = new ArrayList<>();
        Order order = getOrder(book, bl);
        String accessToken = token;

        mockMvc.perform(post("/order/add").contentType(MediaType.APPLICATION_JSON).
                header("Authorization", "Bearer " + accessToken).
                content(objectMapper.writeValueAsString(order))).
                andExpect(status().isOk()).
                andDo(print()).
                andReturn();

    }

    @Test
    void addNewOrderWithNotEnoughBook_shouldReturnNotFound() throws Exception {
        // Try unouthirezed method
        List<Book> bl = new ArrayList<>();
        Book b = new Book();
        Order order = getOrder(b,bl);
        String accessToken = token;
        mockMvc.perform(post("/order/add").
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(order))
        ).andExpect(status().isNotFound());
    }

    @Test
    void queryOrderByDateInterval_shouldReturnValidDto() throws Exception {
        // Try unouthirezed method
        String dateBegin = "2022-03-16";
        String dateEnd = "2022-04-20";

        String accessToken = token;
        mockMvc.perform(get("/order/getByDateInterval?startDate="+dateBegin+"&stopDate="+dateEnd).
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
        ).andExpect(status().isOk());
    }

    @Test
    void queryOrderById() throws Exception {
        // Try unouthirezed method
        String accessToken = token;
        addNewOrder_shouldReturnValidDto();
        Order order = orderRepository.findAll().get(0);
        mockMvc.perform(get("/order/getById/"+order.getId()).
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
        ).andExpect(status().isOk());
    }

    public static Order getOrder(Book book, List<Book> bl) {
        Order order = new Order();
        bl.add(book);
        order.setStatus("TEST");
        order.setOrderPrice(1234);
        order.setOrderNumber(1);
        order.setBookList(bl);
        order.setCustomerId("0000");
        order.setStartDate(new Date());
        return order;
    }

}