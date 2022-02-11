package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.impl.ArrayListProductDao;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.web.servlet.ProductDetailsPageServlet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;

    private final ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        ArrayListProductDao.getInstance().save(new Product());

        when(request.getPathInfo()).thenReturn("/1");

        servlet.doGet(request, response);


        verify(request).getPathInfo();
        verify(request).setAttribute(eq("product"), any(Product.class));
        verify(request).getRequestDispatcher(anyString());
        verify(requestDispatcher).forward(request, response);
    }
}
