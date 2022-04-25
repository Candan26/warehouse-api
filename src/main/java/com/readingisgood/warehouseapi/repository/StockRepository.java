package com.readingisgood.warehouseapi.repository;

import com.readingisgood.warehouseapi.entity.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockRepository  extends MongoRepository<Stock, String> {
    Stock findByBookName(String name);
}
