package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.Product;
import com.es.phoneshop.exception.ProductNotFoundException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private static ProductDao instance;

    public static synchronized ProductDao getInstance(){
        if (instance == null) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    private static final ReadWriteLock lock = new ReentrantReadWriteLock(true);
    private long maxId;
    private final List<Product> products;

    private ArrayListProductDao() {
        maxId = 0L;
        this.products = new ArrayList<>();
    }


    @Override
    public Product getProduct(Long id) throws ProductNotFoundException {
        lock.readLock().lock();
        try {
            if (id == null) {
                throw new ProductNotFoundException("Null id");
            }
            return products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException(id,"No product with this id"));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts(String query,SortField sortField,SortOrder sortOrder) {
        lock.readLock().lock();
        try {
                String[] words = query == null ? new String[0] : query.split(" ");

            return products.stream()
                    .filter(product -> (query == null || query.isEmpty() || containsAllWords(product,words)))
                    .filter(this::isPriced)
                    .filter(this::isInStock)
                    .sorted(comparator(sortField,sortOrder))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    private boolean containsAllWords(Product product, String[] words){
        for(String word : words){
            if (!product.getDescription().contains(word)) {
                return false;
            }
        }
        return true;
    }

    private boolean isPriced(Product product) {
        return product.getPrice() != null;
    }

    private boolean isInStock(Product product) {
        return product.getStock() > 0;
    }

    private Comparator<Product> comparator(SortField sortField, SortOrder sortOrder) {
        if (sortField == null & sortOrder == null) {
            return Comparator.comparing(product -> product.getDescription().split(" ").length);
        }

        Comparator<Product> comparator;
        if (SortField.description == sortField) {
            comparator = Comparator.comparing(Product::getDescription);
        } else {
            comparator = Comparator.comparing(Product::getPrice);
        }

        if (sortOrder == SortOrder.desc) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    @Override
    public void save(Product product) throws ProductNotFoundException {
        lock.writeLock().lock();
        try {
            if (product.getId() == null) {
                product.setId(++maxId);
                products.add(product);
            } else {
                int sameIdIndex = products.indexOf(getProduct(product.getId()));
                products.set(sameIdIndex,product);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(Long id) throws ProductNotFoundException {
        lock.writeLock().lock();
        try {
            products.removeIf(product -> product.getId().equals(id));
            if (id == maxId) {
                maxId = products.get(products.size()-1).getId();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }


}
