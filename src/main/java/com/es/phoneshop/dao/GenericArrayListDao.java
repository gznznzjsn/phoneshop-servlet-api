package com.es.phoneshop.dao;

import com.es.phoneshop.exception.ItemNotFoundException;
import com.es.phoneshop.model.GenericDaoItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class GenericArrayListDao<T extends GenericDaoItem> {

    private final ReadWriteLock lock = new ReentrantReadWriteLock(true);
    private long maxId;
    private final List<T> items;

    public GenericArrayListDao() {
        maxId = 0L;
        this.items = new ArrayList<>();
    }

    public T getItem(Long id) {
        lock.readLock().lock();
        try {
            if (id == null) {
                throw new IllegalArgumentException("id value is null");
            }
            return items.stream()
                    .filter(item -> id.equals(item.getId()))
                    .findAny()
                    .orElseThrow(() -> new ItemNotFoundException(id));
        } finally {
            lock.readLock().unlock();
        }
    }


    public void saveItem(T item){
        getLock().writeLock().lock();
        try {
            if (item.getId() == null) {
                item.setId(++maxId);
                items.add(item);
            } else {
                int sameIdIndex = items.indexOf(getItem(item.getId()));
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
