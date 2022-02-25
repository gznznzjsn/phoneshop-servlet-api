package com.es.phoneshop.dao;

import com.es.phoneshop.model.order.Order;

public interface OrderDao {
    Order getOrderBySecureId(String secureId);

    void save(Order order);
}
