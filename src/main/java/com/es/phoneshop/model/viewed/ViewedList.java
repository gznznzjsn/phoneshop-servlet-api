package com.es.phoneshop.model.viewed;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;

import java.util.ArrayList;
import java.util.List;

public class ViewedList {
    public static final int CONST_MAX_SIZE = 3;

    private List<Product> previouslyViewedProducts;
    private List<Product> viewedProducts;


    public ViewedList() {
        this.previouslyViewedProducts = new ArrayList<>();
        this.viewedProducts = new ArrayList<>();
    }

    public List<Product> getPreviouslyViewedProducts() {
        return previouslyViewedProducts;
    }

    public List<Product> getViewedProducts() {
        return viewedProducts;
    }

    public void setPreviouslyViewedProducts(List<Product> previouslyViewedProducts) {
        this.previouslyViewedProducts = previouslyViewedProducts;
    }

    public void setViewedProducts(List<Product> viewedProducts) {
        this.viewedProducts = viewedProducts;
    }

    public boolean isAnyPreviouslyViewed(){
        return previouslyViewedProducts.size() != 0;
    }

    public boolean isAnyViewed(){
        return viewedProducts.size() != 0;
    }
}
