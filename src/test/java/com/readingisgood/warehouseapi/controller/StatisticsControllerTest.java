package com.readingisgood.warehouseapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.readingisgood.warehouseapi.entity.Book;
import com.readingisgood.warehouseapi.entity.Order;
import com.readingisgood.warehouseapi.model.WarehouseResponse;
import com.readingisgood.warehouseapi.repository.BookRepository;
import com.readingisgood.warehouseapi.repository.OrderRepository;
import com.readingisgood.warehouseapi.repository.StockRepository;
import com.readingisgood.warehouseapi.util.WarehouseJwtUtil;
import com.readingisgood.warehouseapi.util.WarehouseUtil;
import lombok.extern.slf4j.Slf4j;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.readingisgood.warehouseapi.controller.BookControllerTest.getBook;
import static com.readingisgood.warehouseapi.controller.OrderControllerTest.getOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class StatisticsControllerTest {
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
    void getTotalOrder_shouldReturnValidDto() throws Exception {
        String accessToken = token;
        MvcResult obj = mockMvc.perform(get("/statistics/totalOrderCount").
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
        ).andReturn();
        WarehouseResponse warehouseResponse = getWarehouseResponse(obj);
        log.debug(warehouseResponse.toString());
    }

    @Test
    void getTotalOrderByDate_shouldReturnValidDto() throws Exception {
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

        String startDate = "2022-03-16";
        String endDate = "2022-04-20";
        mockMvc.perform(get("/statistics/totalOrderCountByDate?dateBegin="+startDate+"&dateEnd="+endDate).
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
        ).andExpect(status().is2xxSuccessful());
    }

    //private methods
    private WarehouseResponse getWarehouseResponse(MvcResult obj) throws UnsupportedEncodingException, JsonProcessingException {
        String s = obj.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(s,
                new TypeReference<>() {
                });
    }

}