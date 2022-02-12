package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.DefaultCartService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class CartPageServlet extends HttpServlet {
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cart", cartService.getCart(request));

        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] quantities = request.getParameterValues("quantity");
        String[] productIds = request.getParameterValues("productId");
        Map<Long, String> errors = new HashMap<>();

        for (int i = 0; i < productIds.length; i++) {
            Long productId = Long.valueOf(productIds[i]);
            String quantityString = quantities[i];
            int quantity;
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            try {
                if (!quantityString.matches("[0-9]+")) {
                    throw new NumberFormatException();
                }
                quantity = format.parse(quantityString).intValue();
                cartService.update(cartService.getCart(request), productId, quantity);
            } catch (ParseException | NumberFormatException | OutOfStockException e) {
                errors.put(productId, "Error during update");
            }
        }
        if (errors.isEmpty()) {
            response.sendRedirect(String.format("%s/cart?message=Cart updated successfully", request.getContextPath()));
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }

    }

}
