package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.viewed.ViewedList;
import com.es.phoneshop.service.ViewedListService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

public class DefaultViewedListService implements ViewedListService {
    public static final String VIEWED_SESSION_ATTRIBUTE = DefaultViewedListService.class.getName() + ".viewed";

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
    public ViewedList getViewedList(HttpServletRequest request) {
        HttpSession session = request.getSession();
        synchronized (session) {
            ViewedList viewedList = (ViewedList) request.getSession().getAttribute(VIEWED_SESSION_ATTRIBUTE);
            if (viewedList == null) {
                request.getSession().setAttribute(VIEWED_SESSION_ATTRIBUTE, viewedList = new ViewedList());
            }
            return viewedList;
        }
    }

    @Override
    public void update(ViewedList viewedList, Long productId) {
        synchronized (viewedList) {
            viewedList.setPreviousVersionList(new ArrayList<>(viewedList.getCurrentVersionList()));

            Product product = productDao.getProduct(productId);
            if (viewedList.getCurrentVersionList().contains(product)) {
                return;
            }
            if (viewedList.getCurrentVersionList().size() == ViewedList.CONST_MAX_SIZE) {
                viewedList.getCurrentVersionList().remove(0);
            }
            viewedList.getCurrentVersionList().add(product);
        }
    }
}
