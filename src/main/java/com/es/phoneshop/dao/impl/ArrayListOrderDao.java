package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.GenericArrayListDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.ItemNotFoundException;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

public class ArrayListOrderDao extends GenericArrayListDao<Order> implements OrderDao {
    private static OrderDao instance;

    public static OrderDao getInstance() {
        if (instance == null) {
            synchronized (ArrayListOrderDao.class) {
                if (instance == null) {
                    instance = new ArrayListOrderDao();
                }
            }
        }
        return instance;
    }


    private ArrayListOrderDao() {
        super();
    }

    @Override
    public Order getOrderBySecureId(String secureId) {
        getLock().readLock().lock();
        try {
            if (secureId == null) {
                throw new IllegalArgumentException("Null secure id");
            }
            return getItems().stream()
                    .filter(order -> secureId.equals(order.getSecureId()))
                    .findAny()
                    .orElseThrow(() -> new OrderNotFoundException(secureId, "No order with this secure id"));
        } finally {
            getLock().readLock().unlock();
        }
    }


    @Override
    public void save(Order order){
        try {
            saveItem(order);
        } catch (ItemNotFoundException ex) {
            throw new OrderNotFoundException(ex.getId(), ex.getMessage());
        }
    }


}
