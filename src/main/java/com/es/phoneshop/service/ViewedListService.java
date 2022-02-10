package com.es.phoneshop.service;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.viewed.ViewedList;

import javax.servlet.http.HttpServletRequest;

public interface ViewedListService {
    ViewedList getViewedList(HttpServletRequest request);
    void update(ViewedList viewedList, Long productId);
}
