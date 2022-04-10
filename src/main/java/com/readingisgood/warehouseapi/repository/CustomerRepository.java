package com.readingisgood.warehouseapi.repository;

import com.readingisgood.warehouseapi.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CustomerRepository  extends MongoRepository<Customer, String> {
     List<Customer> findByNameAndSurname(String name, String surname);
     List<Customer> findByTcId(String tcId);
}
