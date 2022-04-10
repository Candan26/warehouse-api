package com.readingisgood.warehouseapi.service.impl;

import com.readingisgood.warehouseapi.entity.Book;
import com.readingisgood.warehouseapi.entity.Order;
import com.readingisgood.warehouseapi.entity.Stock;
import com.readingisgood.warehouseapi.model.Error;
import com.readingisgood.warehouseapi.model.WarehouseResponse;
import com.readingisgood.warehouseapi.repository.OrderRepository;
import com.readingisgood.warehouseapi.repository.StockRepository;
import com.readingisgood.warehouseapi.service.OrderService;
import com.readingisgood.warehouseapi.util.WarehouseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final String ERROR_INSERT_BOOK_IN_ORDER = "Please put which book you want to buy in order";
    private static final String ERROR_OUT_OF_STOCK = "Given books are not valid in stock";
    private static final String ERROR_INSERT_ORDER = "System has error while inserting order please try again later";
    private static final String ERROR_UPDATE_STOCK = "System has error on stock  please try again later";

    private final OrderRepository orderRepository;

    private final StockRepository stockRepository;

    @Override
    public WarehouseResponse queryOrdersByDateInterval(Date startDate, Date stopDate) {
        try {
            return new WarehouseResponse(WarehouseUtil.SUCCEED, orderRepository.findByStartDateBetween(startDate, stopDate), null);
        } catch (Exception ex) {
            log.error("Exception on ",ex);
            return new WarehouseResponse(WarehouseUtil.FAILED, "", new Error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex));
        }
    }

    @Override
    public WarehouseResponse queryOrdersById(String id) {
        try {
            return new WarehouseResponse(WarehouseUtil.SUCCEED, orderRepository.findById(id).stream().findFirst().orElse(new Order()), null);
        } catch (Exception ex) {
            log.error("Exception on ",ex);
            return new WarehouseResponse(WarehouseUtil.FAILED, "", new Error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex));
        }
    }

    @Override
    public WarehouseResponse addNewOrder(Order order) {

        List<Book> bookList = order.getBookList();
        if (bookList == null || bookList.isEmpty()) {
            log.error("Order has no book ");
            return new WarehouseResponse(WarehouseUtil.FAILED, "", new Error(HttpStatus.BAD_REQUEST, ERROR_INSERT_BOOK_IN_ORDER));
        }
        HashMap<String, Integer> bookMap;
        bookMap = categorizeBooks(bookList);
        if (!checkBooksOnStock(bookMap)) {
            log.error("There is not enough book on Stock ");
            return new WarehouseResponse(WarehouseUtil.FAILED, "", new Error(HttpStatus.BAD_REQUEST, ERROR_OUT_OF_STOCK));
        }
        if (updateStock(bookMap)) {
            log.error("System cannot update the stock please check db connection");
            return new WarehouseResponse(WarehouseUtil.FAILED, "", new Error(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_UPDATE_STOCK));
        }
        return addOrder(order, bookMap);
    }

    private void rollbackStock(HashMap<String, Integer> bookMap) {
        for (Map.Entry<String, Integer> entry : bookMap.entrySet()) {
            Stock stock = stockRepository.findByBookName(entry.getKey());
            double unitPrice = stock.getTotalPrice() / stock.getTotalQuantity();
            stock.setTotalQuantity(stock.getTotalQuantity() + entry.getValue());
            stock.setTotalPrice(stock.getTotalPrice() + (unitPrice * entry.getValue()));
            log.info("The order successfully restored in stock " + stock);
            stockRepository.save(stock);
        }
    }

    private WarehouseResponse addOrder(Order order, HashMap<String, Integer> bookMap) {
        try {
            order.setStatus(WarehouseUtil.PURCHASED);
            return new WarehouseResponse(WarehouseUtil.SUCCEED, orderRepository.save(order), null);
        } catch (Exception ex) {
            log.error("Exception on ", ex);
            log.error("rollback the stock information ");
            rollbackStock(bookMap);
            return new WarehouseResponse(WarehouseUtil.FAILED, "", new Error(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_INSERT_ORDER));
        }
    }

    private boolean updateStock(HashMap<String, Integer> bookMap) {
        try {
            for (Map.Entry<String, Integer> entry : bookMap.entrySet()) {
                Stock stock = stockRepository.findByBookName(entry.getKey());
                double unitPrice = stock.getTotalPrice() / stock.getTotalQuantity();
                stock.setTotalQuantity(stock.getTotalQuantity() - entry.getValue());
                stock.setTotalPrice(stock.getTotalPrice() - (unitPrice * entry.getValue()));
                stockRepository.save(stock);
            }
            return true;
        } catch (Exception ex) {
            log.error("Exception on ", ex);
            return false;
        }
    }

    private HashMap<String, Integer> categorizeBooks(List<Book> bookList) {
        HashMap<String, Integer> bm = new HashMap<>();
        for (Book b : bookList) {
            if (!bm.containsKey(b.getName())) {
                bm.put(b.getName(), 1);
            } else {
                bm.put(b.getName(), bm.get(b.getName()) + 1);
            }
        }
        return bm;
    }

    private boolean checkBooksOnStock(HashMap<String, Integer> bookMap) {
        try {
            for (Map.Entry<String, Integer> entry : bookMap.entrySet()) {
                Stock stock = stockRepository.findByBookName(entry.getKey());
                if (stock == null || stock.getTotalQuantity() < entry.getValue()) {
                    log.info("Not enough staff in stock");
                    return false;
                }
            }
        } catch (Exception ex) {
            log.error("Exception on ", ex);
            return false;
        }
        return true;
    }
}
