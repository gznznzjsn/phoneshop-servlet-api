package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListOrderDao implements OrderDao {
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

    private static final ReadWriteLock lock = new ReentrantReadWriteLock(true);
    private long maxId;
    private final List<Order> orders;

    private ArrayListOrderDao() {
        maxId = 0L;
        this.orders = new ArrayList<>();
    }


    @Override
    public Order getOrder(Long id) throws OrderNotFoundException {
        lock.readLock().lock();
        try {
            if (id == null) {
                throw new OrderNotFoundException("Null id");
            }
            return orders.stream()
                    .filter(order -> id.equals(order.getId()))
                    .findAny()
                    .orElseThrow(() -> new OrderNotFoundException(id, "No order with this id"));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Order getOrderBySecureId(String secureId) throws OrderNotFoundException {
        lock.readLock().lock();
        try {
            if (secureId == null) {
                throw new OrderNotFoundException("Null secure id");
            }
            return orders.stream()
                    .filter(order -> secureId.equals(order.getSecureId()))
                    .findAny()
                    .orElseThrow(() -> new OrderNotFoundException( secureId,"No order with this secure id"));
        } finally {
            lock.readLock().unlock();
        }
    }


    @Override
    public void save(Order order) throws OrderNotFoundException {
        lock.writeLock().lock();
        try {
            if (order.getId() == null) {
                order.setId(++maxId);
                orders.add(order);
            } else {
                int sameIdIndex = orders.indexOf(getOrder(order.getId()));
                orders.set(sameIdIndex, order);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }



}
