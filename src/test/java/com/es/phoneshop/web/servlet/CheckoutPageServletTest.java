package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.service.impl.DefaultOrderService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
    private final CartService cartService = DefaultCartService.getInstance();
    private final OrderService orderService = DefaultOrderService.getInstance();
    @Mock
    private HttpSession session;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;

    @Rule
    public ExpectedException ex = ExpectedException.none();

    private final CheckoutPageServlet servlet = new CheckoutPageServlet();

    @Before
    public void setup() throws ServletException, OutOfStockException {
        servlet.init(config);
        when(request.getSession()).thenReturn(session);
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request,response);
        verify(request).setAttribute(eq("order"),any(Order.class));
        verify(request).setAttribute(eq("paymentMethods"),anyList());
    }
    @Test
    public void testDoPostFailure() throws ServletException, IOException {
        servlet.doGet(request,response);
        when(request.getParameter(anyString())).thenReturn("");
        servlet.doPost(request,response);
        verify(request).setAttribute(eq("errors"),anyMap());
    }

    @Test
    public void testDoPostSuccess() throws ServletException, IOException {
        servlet.doGet(request,response);
        when(request.getParameter(anyString())).thenReturn("CASH");
        when(request.getParameter("deliveryDate")).thenReturn("2022-02-24");
        servlet.doPost(request,response);
        verify(response).sendRedirect(String.format("%s/order/overview/%s", request.getContextPath(), anyString()));
    }
}