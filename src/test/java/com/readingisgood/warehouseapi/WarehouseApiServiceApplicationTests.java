package com.readingisgood.warehouseapi;

import com.readingisgood.warehouseapi.repository.BookRepository;
import com.readingisgood.warehouseapi.repository.CustomerRepository;
import com.readingisgood.warehouseapi.repository.OrderRepository;
import com.readingisgood.warehouseapi.repository.StockRepository;
import com.readingisgood.warehouseapi.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WarehouseApiServiceApplicationTests {

    //repos
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    StockRepository stockRepository;

    //services
    @Autowired
    AuthService authService;

    @Autowired
    BookService bookService;

    @Autowired
    CustomerService customerService;

    @Autowired
    OrderService orderService;

    @Autowired
    StatisticsService statisticsService;

    @Test
    public void repositoryInitializedCorrectly() {
        assertThat(customerRepository).isNotNull();
        assertThat(bookRepository).isNotNull();
        assertThat(orderRepository).isNotNull();
        assertThat(stockRepository).isNotNull();
    }

    @Test
    public void serviceInitializedCorrectly() {
        assertThat(authService).isNotNull();
        assertThat(bookService).isNotNull();
        assertThat(customerService).isNotNull();
        assertThat(orderService).isNotNull();
        assertThat(statisticsService).isNotNull();
    }

    @Test
    void contextLoads() {
    }

}
