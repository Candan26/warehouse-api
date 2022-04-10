package com.readingisgood.warehouseapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Slf4j
@EnableWebMvc
@EnableSwagger2
public class WarehouseApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WarehouseApiApplication.class, args);
    }

}
