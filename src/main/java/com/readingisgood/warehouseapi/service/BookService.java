package com.readingisgood.warehouseapi.service;

import com.readingisgood.warehouseapi.entity.Book;
import com.readingisgood.warehouseapi.entity.Customer;
import com.readingisgood.warehouseapi.entity.Stock;
import com.readingisgood.warehouseapi.model.WarehouseResponse;

public interface BookService {
    WarehouseResponse addBook(Book book);
    WarehouseResponse updateBook(Stock request);
}
