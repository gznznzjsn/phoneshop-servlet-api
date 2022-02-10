package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.viewed.ViewedList;
import com.es.phoneshop.service.ViewedListService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public class DefaultViewedListService implements ViewedListService {
    private static final String VIEWED_SESSION_ATTRIBUTE = DefaultViewedListService.class.getName() + ".viewed";

    private final ProductDao productDao;

    private DefaultViewedListService() {
        productDao = ArrayListProductDao.getInstance();
    }

    private static ViewedListService instance;

    public static ViewedListService getInstance() {
        if (instance == null) {
            synchronized (DefaultViewedListService.class) {
                if (instance == null) {
                    instance = new DefaultViewedListService();
                }
            }
        }
        return instance;
    }

    @Override
    public synchronized ViewedList getViewedList(HttpServletRequest request) {
        ViewedList viewedList = (ViewedList) request.getSession().getAttribute(VIEWED_SESSION_ATTRIBUTE);
        if (viewedList == null) {
        request.getSession().setAttribute(VIEWED_SESSION_ATTRIBUTE, viewedList = new ViewedList());
        }
        return viewedList;
    }

    @Override
    public synchronized void update(ViewedList viewedList, Long productId) {
        viewedList.setPreviouslyViewedProducts(new ArrayList<>(viewedList.getViewedProducts()));

        Product product = productDao.getProduct(productId);
        if (viewedList.getViewedProducts().contains(product)) {
            return;
        }
        if (viewedList.getViewedProducts().size() == ViewedList.CONST_MAX_SIZE) {
            viewedList.getViewedProducts().remove(0);
        }
        viewedList.getViewedProducts().add(product);
    }
}
