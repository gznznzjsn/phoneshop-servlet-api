package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.viewed.ViewedList;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.DefaultCartService;
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
import java.util.Currency;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeleteCartItemServletTest {
    private final CartService cartService = DefaultCartService.getInstance();
    @Mock
    private HttpSession session;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletConfig config;


    private final DeleteCartItemServlet servlet = new DeleteCartItemServlet();

    @Before
    public void setup() throws ServletException, OutOfStockException {
        servlet.init(config);
        when(request.getSession()).thenReturn(session);
        when(request.getPathInfo()).thenReturn("/1");
        ArrayListProductDao.getInstance().save(new Product(1L, "code", "description", new BigDecimal(100), null, 1, null));
    }


    @Test
    public void testDoPost() throws IOException, OutOfStockException {
        Cart cart = new Cart();
        cartService.add(cart,1L,1);
        when(session.getAttribute(anyString())).thenReturn(cart);
        servlet.doPost(request, response);
        verify(response).sendRedirect((String.format("%s/cart?message=Item deleted successfully", request.getContextPath())));
        verify(request).getPathInfo();
    }

}