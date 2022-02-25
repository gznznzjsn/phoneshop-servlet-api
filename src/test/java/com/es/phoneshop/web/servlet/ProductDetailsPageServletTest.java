package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.impl.ArrayListProductDao;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.viewed.ViewedList;
import com.es.phoneshop.service.ViewedListService;
import com.es.phoneshop.service.impl.DefaultViewedListService;
import com.es.phoneshop.web.servlet.ProductDetailsPageServlet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.verification.NoMoreInteractions;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {
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

    private final ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        when(request.getSession()).thenReturn(session);
        when(request.getPathInfo()).thenReturn("/1");
        ArrayListProductDao.getInstance().save(new Product(1L, "code", "description", new BigDecimal(100), null, 1, null));
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getLocale()).thenReturn(Locale.ROOT);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).getPathInfo();
        verify(request).setAttribute(eq("product"), any(Product.class));
        verify(request).setAttribute(eq("cart"), any(Cart.class));
        verify(request).setAttribute(eq("viewedList"), any(ViewedList.class));
        verify(request).getRequestDispatcher("/WEB-INF/pages/product.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostWrongFormat() throws ServletException, IOException {
        when(request.getParameter("quantity")).thenReturn("twenty");
        servlet.doPost(request, response);
        verify(request).setAttribute(eq("error"), anyString());
        verify(request).setAttribute(eq("quantity"), anyString());
        verify(request).getRequestDispatcher("/WEB-INF/pages/product.jsp");
    }


    @Test
    public void testDoPostZeroQuantity() throws ServletException, IOException {
        when(request.getParameter("quantity")).thenReturn("0");
        servlet.doPost(request, response);
        verify(response).sendRedirect(String.format("%s/products/%d?message=Inserted quantity = 0, nothing happened&prevquantity=%s", request.getContextPath(), 1L, "0"));
    }


    @Test
    public void testDoPostSuccess() throws ServletException, IOException {
        ArrayListProductDao.getInstance().save(new Product(1L, "code", "description", new BigDecimal(100), Currency.getInstance("USD"), 10, null));
        when(request.getParameter("quantity")).thenReturn("1");
        servlet.doPost(request, response);
        verify(request).getParameter("quantity");

        verify(response).sendRedirect(String.format("%s/products/%d?message=Product added to cart&prevquantity=%s", request.getContextPath(), 1L, "1"));
    }


    @Test
    public void testParseId() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).getPathInfo();
    }
}
