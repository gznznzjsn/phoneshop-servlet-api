package com.es.phoneshop.model.viewed;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;

import java.util.ArrayList;
import java.util.List;

public class ViewedList {
    private static ViewedList instance;
    public static final int CONST_MAX_SIZE = 3;

    public static ViewedList getInstance() {
        if (instance == null) {
            synchronized (ViewedList.class){
                if (instance == null){
                    instance = new ViewedList();
                }
            }
        }
        return instance;
    }
    private final List<Product> viewedProducts;

    private ViewedList() {
        this.viewedProducts = new ArrayList<>();
    }

    public void update(Long id){
        Product product = ArrayListProductDao.getInstance().getProduct(id);
        if(viewedProducts.contains(product)){
            return;
        }
        if(viewedProducts.size() == CONST_MAX_SIZE){
            viewedProducts.remove(0);
        }
        viewedProducts.add(product);
    }

    public List<Product> getViewedProducts() {
        return viewedProducts;
    }
}
