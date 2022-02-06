package com.es.phoneshop.model.viewed;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;

import javax.servlet.http.HttpServletRequest;

public interface ViewedListService {
    ViewedList getViewedList(HttpServletRequest request);
    void update(ViewedList viewedList, Long productId);
}
