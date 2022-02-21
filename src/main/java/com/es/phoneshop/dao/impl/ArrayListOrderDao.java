package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.generic.ArrayListDao;
import com.es.phoneshop.model.order.Order;

public class ArrayListOrderDao extends ArrayListDao<Order> implements OrderDao {
    private static OrderDao instance;

    public static OrderDao getInstance() {
        if (instance == null) {
            synchronized (ArrayListOrderDao.class){
                if (instance == null){
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
    public Order getOrder(Long id) throws OrderNotFoundException {
        return get(id,ArrayListDao.ORDER);
    }

    @Override
    public Order getOrderBySecureId(String secureId) throws OrderNotFoundException {
        getLock().readLock().lock();
        try {
            if (secureId == null) {
                throw new OrderNotFoundException("Null secure id");
            }
            return getItems().stream()
                    .filter(order -> secureId.equals(order.getSecureId()))
                    .findAny()
                    .orElseThrow(() -> new OrderNotFoundException( secureId,"No order with this secure id"));
        } finally {
            getLock().readLock().unlock();
        }
    }


    @Override
    public void save(Order order) throws OrderNotFoundException {
        save(order,ArrayListDao.ORDER);
    }



}
