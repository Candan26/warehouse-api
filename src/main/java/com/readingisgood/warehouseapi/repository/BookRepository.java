package com.readingisgood.warehouseapi.repository;

import com.readingisgood.warehouseapi.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String> {
    Book findByName(String name);
}
