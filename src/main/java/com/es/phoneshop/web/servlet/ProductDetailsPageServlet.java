package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.viewed.ViewedList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = parseProductId(request);
        request.setAttribute("product", productDao.getProduct(id));
        ViewedList.getInstance().update(id);
        request.setAttribute("cart", cartService.getCart(request));
        request.setAttribute("viewedProducts", ViewedList.getInstance().getViewedProducts());
        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = parseProductId(request);
        String quantityString = request.getParameter("quantity");
        int quantity;
        try {
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(quantityString).intValue();
        } catch (ParseException e) {
            response.sendRedirect(request.getContextPath() + "/products/" + id + "?error=Not a number");
            return;
        }
        try {
            cartService.add(cartService.getCart(request), id, quantity);
        } catch (OutOfStockException e) {
            response.sendRedirect(request.getContextPath() + "/products/" + id +
                    "?error=Out of stock, available " + e.getStockAvailable());
            return;
        }

        response.sendRedirect(request.getContextPath() + "/products/" + id + "?message=Product added to cart");
    }

    private Long parseProductId(HttpServletRequest request) {
        String productId = request.getPathInfo().substring(1);
        return Long.valueOf(productId);
    }
}
