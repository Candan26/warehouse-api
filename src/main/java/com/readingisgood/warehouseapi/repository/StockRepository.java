package com.readingisgood.warehouseapi.repository;

import com.readingisgood.warehouseapi.entity.Book;
import com.readingisgood.warehouseapi.entity.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StockRepository  extends MongoRepository<Stock, String> {
    Stock findByBookName(String name);
}
