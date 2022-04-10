package com.readingisgood.warehouseapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readingisgood.warehouseapi.dto.AuthServiceDto;
import com.readingisgood.warehouseapi.entity.Book;
import com.readingisgood.warehouseapi.entity.Customer;
import com.readingisgood.warehouseapi.entity.Order;
import com.readingisgood.warehouseapi.repository.BookRepository;
import com.readingisgood.warehouseapi.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WarehouseApiWebApplicationTests {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    BookRepository bookRepository;
    
    @Autowired
    OrderRepository orderRepository;

    @Test
    void testAuthentication() throws Exception {
        AuthServiceDto dto = new AuthServiceDto();
        dto.setSubject("test");
        mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .param("subject", "test")
        ).andExpect(status().isOk());
    }

    @Test
    void testCustomerInsertionUnauthorized() throws Exception {
        // Try unouthirezed method
        Customer customer= new Customer();
        customer.setEmail("123");
        customer.setName("test");
        customer.setSurname("surname");
        customer.setTcId("231234124");
        mockMvc.perform(post("/customer/add").
                contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void testCustomerInsertionSucceed() throws Exception {
        // Try unouthirezed method
        Customer customer= new Customer();
        customer.setEmail("123");
        customer.setName("test");
        customer.setSurname("surname");
        customer.setTcId("231234124");
        String accessToken = obtainAccessToken();
        mockMvc.perform(post("/customer/add").
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(customer))
        );
    }

    @Test
    void testCustomerInsertionFailed() throws Exception {
        // Try unouthirezed method
        Customer customer= new Customer();
        customer.setEmail("123");
        customer.setName("test");
        customer.setSurname("surname");
        String accessToken = obtainAccessToken();
        mockMvc.perform(post("/customer/add").
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(customer))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void testOrderFromCustomerNameAndSurname() throws Exception {
        // Get 200 if data valid not 204
        String accessToken = obtainAccessToken();
        mockMvc.perform(get("/customer/getOrderList/0/10/cagri/candan").
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
        ).andExpect(status().is2xxSuccessful());
    }

    @Test
    void testOrderFromCustomerTcId() throws Exception {
        // Get 200 if data valid not 204
        String accessToken = obtainAccessToken();
        mockMvc.perform(get("/customer/getOrderList/0/10/123123").
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
        ).andExpect(status().is2xxSuccessful());
    }

    @Test
    void addBookSucceed() throws Exception {
        // Try unouthirezed method
        Book book= new Book();
        book.setAuthor("testAuthor");
        book.setName("test");
        book.setPrice(1231);
        String accessToken = obtainAccessToken();
        mockMvc.perform(post("/book/add").
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(book))
        ).andExpect(status().isOk());
    }

    @Test
    void addBookFailed() throws Exception {
        // Try unouthirezed method
        Book book= new Book();
        book.setAuthor("testAuthor");
        book.setPrice(1231);
        String accessToken = obtainAccessToken();
        mockMvc.perform(post("/book/add").
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(book))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void updateStockFailed() throws Exception {
        // Try unouthirezed method
        Book book= new Book();
        book.setAuthor("testAuthor");
        book.setPrice(1231);
        String accessToken = obtainAccessToken();
        mockMvc.perform(post("/book/update").
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(book))
        ).andExpect(status().isInternalServerError());
    }


    @Test
    void updateStockPassed() throws Exception {
        // Try unouthirezed method
        Book book = bookRepository.findByName("testbook");
        book.setAuthor("testAuthor");
        book.setPrice(1231);
        String accessToken = obtainAccessToken();
        mockMvc.perform(post("/book/update").
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(book))
        ).andExpect(status().isInternalServerError());
    }


    @Test
    void addNewOrderFailed() throws Exception {
        // Try unouthirezed method
        Order order = new Order();
        order.setStatus("TEST");
        order.setOrderPrice(1234);
        order.setOrderNumber(1);
        order.setBookList(new ArrayList<>());
        order.setCustomerId("0000");
        order.setStartDate(new Date());
        String accessToken = obtainAccessToken();
        mockMvc.perform(post("/order/add").
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(order))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void addNewOrderFailedNotEnoughBook() throws Exception {
        // Try unouthirezed method
        List<Book> bl = new ArrayList<>();
        Book b = new Book();
        Order order = new Order();

        b.setName("TestBookForOrder");
        bl.add(b);
        order.setStatus("TEST");
        order.setOrderPrice(1234);
        order.setOrderNumber(1);
        order.setBookList(bl);
        order.setCustomerId("0000");
        order.setStartDate(new Date());
        String accessToken = obtainAccessToken();
        mockMvc.perform(post("/order/add").
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(order))
        ).andExpect(status().isBadRequest());
    }


    @Test
    void addNewOrderSucceed() throws Exception {
        // Try unouthirezed method
        List<Book> bl = new ArrayList<>();
        Book b = new Book();
        Order order = new Order();
        b.setName("testbook");// book should be on stock document
        bl.add(b);
        order.setStatus("TEST");
        order.setOrderPrice(1234);
        order.setOrderNumber(1);
        order.setBookList(bl);
        order.setCustomerId("0000");
        order.setStartDate(new Date());
        String accessToken = obtainAccessToken();
        mockMvc.perform(post("/order/add").
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(order))
        ).andExpect(status().is4xxClientError());
    }


    @Test
    void queryOrderByDateInterval() throws Exception {
        // Try unouthirezed method

        String accessToken = obtainAccessToken();
        mockMvc.perform(get("/order/getByDateInterval?startDate=2022-03-16&stopDate=2022-04-20").
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
        ).andExpect(status().isOk());
    }

    @Test
    void queryOrderById() throws Exception {
        // Try unouthirezed method
        String accessToken = obtainAccessToken();
        Order order = orderRepository.findAll().get(0);
        mockMvc.perform(get("/order/getById/"+order.getId()).
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
        ).andExpect(status().isOk());
    }

    @Test
    void getTotalOrder() throws Exception {
        // Try unouthirezed method
        String accessToken = obtainAccessToken();
        mockMvc.perform(get("/statistics/totalOrderCount").
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
        ).andExpect(status().isOk());
    }

    @Test
    void getTotalOrderByDate() throws Exception {
        // Try unouthirezed method
        String accessToken = obtainAccessToken();
        mockMvc.perform(get("/statistics/totalOrderCount?startDate=2022-03-16&stopDate=2022-04-20").
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
        ).andExpect(status().isOk());
    }

    private String obtainAccessToken() throws Exception {
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        ResultActions result
                =    mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AuthServiceDto()))
                .param("subject", "test")
        ).andExpect(status().isOk());
        String resultString = result.andReturn().getResponse().getContentAsString();
        Object obj = jsonParser.parseMap(resultString).get("data");
        return    (String) ( (LinkedHashMap) obj).get("token");
    }

}



