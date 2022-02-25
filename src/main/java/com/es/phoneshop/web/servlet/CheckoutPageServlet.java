package com.es.phoneshop.web.servlet;

import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.service.impl.DefaultOrderService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CheckoutPageServlet extends HttpServlet {
    private CartService cartService;
    private OrderService orderService;
    private Order order;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        order = orderService.getOrder(cart);
        request.setAttribute("order", order);
        request.setAttribute("paymentMethods", orderService.getPaymentMethods());
        request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> errors = new HashMap<>();

        setRequiredParameter("firstName", request, errors, order::setFirstName);
        setRequiredParameter("lastName", request, errors, order::setLastName);
        setRequiredParameter("phone", request, errors, order::setPhone);
         setRequiredParameter("deliveryDate", request, errors, value->{
             LocalDate date = LocalDate.parse(value);
             order.setDeliveryDate(date);
         });
        setRequiredParameter("deliveryAddress", request, errors, order::setDeliveryAddress);
        setRequiredParameter("paymentMethod", request, errors, (value) -> order.setPaymentMethod(PaymentMethod.valueOf(value)));

        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.clear(cartService.getCart(request));
            response.sendRedirect(String.format("%s/order/overview/%s", request.getContextPath(), order.getSecureId()));

        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }

    private void setRequiredParameter(String parameter, HttpServletRequest request, Map<String, String> errors,
                                      Consumer<String> consumer) {
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, "Value is required");
        } else {
            consumer.accept(value);
        }
    }

}
