package com.es.phoneshop.dao;

import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.exception.ProductNotFoundException;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id) throws ProductNotFoundException;
    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);
    void save(Product product) throws ProductNotFoundException;
    void delete(Long id) throws ProductNotFoundException;
}
