package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.GenericArrayListDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.exception.ItemNotFoundException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ArrayListProductDao extends GenericArrayListDao<Product> implements ProductDao {
    private static ProductDao instance;

    public static ProductDao getInstance() {
        if (instance == null) {
            synchronized (ArrayListProductDao.class) {
                if (instance == null) {
                    instance = new ArrayListProductDao();
                }
            }
        }
        return instance;
    }


    @Override
    public Product getProduct(Long id) {
        try {
            return getItem(id);
        } catch (ItemNotFoundException ex) {
            throw new ProductNotFoundException(ex.getId(), ex.getMessage());
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        getLock().readLock().lock();
        try {
            String[] words = query == null ? new String[0] : query.split(" ");

            return getItems().stream()
                    .filter(product -> (query == null || query.isEmpty() || containsAllWords(product, words)))
                    .filter(this::isPriced)
                    .filter(this::isInStock)
                    .sorted(comparator(sortField, sortOrder))
                    .collect(Collectors.toList());
        } finally {
            getLock().readLock().unlock();
        }
    }

    @Override
    public List<Product> findAdvancedProducts(String productCode, BigDecimal minPrice, BigDecimal maxPrice, int minStock) {
        getLock().readLock().lock();
        try {
            return getItems().stream()
                    .filter(this::isPriced)
                    .filter(this::isInStock)
                    .filter(product -> product.getCode().contains(productCode))
                    .filter(product -> product.getPrice().compareTo(minPrice)>=0)
                    .filter(product -> product.getPrice().compareTo(maxPrice)<=0)
                    .filter(product -> product.getStock() >= minStock)
                    .collect(Collectors.toList());
        } finally {
            getLock().readLock().unlock();
        }

    }

    private boolean containsAllWords(Product product, String[] words) {
        for (String word : words) {
            if (!product.getDescription().toLowerCase(Locale.ROOT).contains(word.toLowerCase(Locale.ROOT))) {
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
        if (sortField == null && sortOrder == null) {
            return Comparator.comparing(product -> product.getDescription().split(" ").length);
        }

        Comparator<Product> comparator;
        if (SortField.DESCRIPTION == sortField) {
            comparator = Comparator.comparing(Product::getDescription);
        } else {
            comparator = Comparator.comparing(Product::getPrice);
        }

        if (sortOrder == SortOrder.DESC) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    @Override
    public void save(Product product) {
        try {
            saveItem(product);
        } catch (ItemNotFoundException ex) {
            throw new ProductNotFoundException(ex.getId(), ex.getMessage());
        }
    }
}
