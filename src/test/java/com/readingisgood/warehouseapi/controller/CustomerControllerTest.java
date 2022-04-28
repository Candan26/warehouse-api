package com.readingisgood.warehouseapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readingisgood.warehouseapi.entity.Customer;
import com.readingisgood.warehouseapi.repository.CustomerRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

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
    static void afterAll(@Autowired CustomerRepository customerRepository) {
        customerRepository.deleteAll();
    }

    @BeforeAll
    static void beforeAll(@Autowired CustomerRepository customerRepository) {
        customerRepository.deleteAll();
    }

    @Test
    void addCustomer_shouldReturnValidDto() throws Exception {
        Customer customer = getCustomer(WarehouseUtil.VALID);
        mockMvc.perform(post("/customer/add").
                        contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(customer))
                ).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void addCustomerWithoutTc_shouldReturnBadRequest() throws Exception {
        // Try unouthirezed method
        Customer customer= getCustomer(WarehouseUtil.NOT_VALID);
        String accessToken = token;
        mockMvc.perform(post("/customer/add").
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(customer))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void queryCustomerOrdersByName_shouldReturnValidDto() throws Exception {
        // Get 200 if data valid not 204
        int page = 0;
        int size = 10;
        String name = "cagri";
        String surname = "candan";
        String accessToken =  token;
        mockMvc.perform(get("/customer/getOrderList/"+page+"/"+size+"/"+name+"/"+surname).
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
        ).andExpect(status().is2xxSuccessful());
    }

    @Test
    void queryCustomerOrdersByTc_shouldReturnValidDto() throws Exception {
        // Get 200 if data valid not 204
        int page = 0;
        int size = 10;
        String tc = "123123";
        String accessToken = token;
        mockMvc.perform(get("/customer/getOrderList/"+page+"/"+size+"/"+tc).
                contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
        ).andExpect(status().is2xxSuccessful());
    }

    public static Customer getCustomer(String status) {
        Customer customer = new Customer();
        customer.setTcId(status.equals(WarehouseUtil.VALID) ? "122312312" : null);
        customer.setName(status.equals(WarehouseUtil.VALID) ? "testCustomerName" : null);
        customer.setSurname(status.equals(WarehouseUtil.VALID) ? "testCustomerSurname" : null);
        customer.setEmail("test@testmail.com");
        customer.setAge(0);
        customer.setHasOrder(false);
        return customer;
    }
}