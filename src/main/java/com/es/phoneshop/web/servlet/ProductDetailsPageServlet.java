package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.ViewedListService;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.service.impl.DefaultViewedListService;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Long id = parseProductId(request);
        String quantityString = request.getParameter("quantity");
        int quantity;
        try {
            if (!quantityString.matches("[0-9]+")) {
                throw new NumberFormatException();
            }
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(quantityString).intValue();
            if (quantity == 0) {
                response.sendRedirect(String.format("%s/products/%d?message=Inserted quantity = 0, nothing happened&prevquantity=%s", request.getContextPath(), id, quantityString));
                return;
            }
            cartService.add(cartService.getCart(request), id, quantity);
        } catch (ParseException | NumberFormatException | OutOfStockException e) {
            request.setAttribute("error",generateErrorMessage(e));
            request.setAttribute("quantity",quantityString);
            doGet(request,response);
            return;
        }
        response.sendRedirect(String.format("%s/products/%d?message=Product added to cart&prevquantity=%s",
                        request.getContextPath(), id, quantityString));
    }

    private Long parseProductId(HttpServletRequest request) {
        String productId = request.getPathInfo().substring(1);
        return Long.valueOf(productId);
    }

    private String generateErrorMessage(Exception exception) {
        if (exception instanceof NumberFormatException) {
            return "Wrong format of number";
        } else if (exception instanceof OutOfStockException) {
            return "Out of stock, available " + ((OutOfStockException) exception).getStockAvailable();
        } else {
            return "Not a number";
        }
    }
}
