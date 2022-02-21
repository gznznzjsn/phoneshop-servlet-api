package com.es.phoneshop.generic;

import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.exception.ProductNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class ArrayListDao<T extends DaoItem> {
    public static final int PRODUCT = 0;
    public static final int ORDER = 1;

    private final ReadWriteLock lock = new ReentrantReadWriteLock(true);
    private long maxId;
    private final List<T> items;

    public ArrayListDao() {
        maxId = 0L;
        this.items = new ArrayList<>();
    }

    public T get(Long id, int itemType) {
        lock.readLock().lock();
        try {
            if (id == null) {
                if (itemType == ArrayListDao.PRODUCT) {
                    throw new ProductNotFoundException("Null id");
                } else if (itemType == ArrayListDao.ORDER) {
                    throw new OrderNotFoundException("Null id");
                } else {
                    throw new IllegalArgumentException("Wrong value in itemType");
                }
            }
            return items.stream()
                    .filter(item -> id.equals(item.getId()))
                    .findAny()
                    .orElseThrow(() -> {
                        if (itemType == ArrayListDao.PRODUCT) {
                            return new ProductNotFoundException(id, "No product with this id");
                        } else if (itemType == ArrayListDao.ORDER) {
                            return new OrderNotFoundException(id, "No order with this id");
                        } else {
                            return new IllegalArgumentException("Wrong value in itemType");
                        }
                    });
        } finally {
            lock.readLock().unlock();
        }
    }


    public void save(T item, int itemType) throws OrderNotFoundException {
        getLock().writeLock().lock();
        try {
            if (item.getId() == null) {
                item.setId(++maxId);
                items.add(item);
            } else {
                int sameIdIndex = items.indexOf(get(item.getId(),itemType));
                items.set(sameIdIndex, item);
            }
        } finally {
            getLock().writeLock().unlock();
        }
    }

    public void delete(Long id) {
        lock.writeLock().lock();
        try {
            items.removeIf(item -> item.getId().equals(id));
            if (id == maxId) {
                maxId = items.get(items.size() - 1).getId();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public ReadWriteLock getLock() {
        return lock;
    }

    public List<T> getItems() {
        return items;
    }
}
