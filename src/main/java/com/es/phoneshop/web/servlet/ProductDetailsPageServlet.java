package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.viewed.DefaultViewedListService;
import com.es.phoneshop.model.viewed.ViewedList;
import com.es.phoneshop.model.viewed.ViewedListService;

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
    private ViewedListService viewedListService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        viewedListService = DefaultViewedListService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = parseProductId(request);
        viewedListService.update(viewedListService.getViewedList(request), id);

        request.setAttribute("product", productDao.getProduct(id));
        request.setAttribute("cart", cartService.getCart(request));
        request.setAttribute("viewedList", viewedListService.getViewedList(request));

        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = parseProductId(request);
        String quantityString = request.getParameter("quantity");
        int quantity;
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        try {
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
