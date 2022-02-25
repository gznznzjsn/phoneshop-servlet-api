package com.es.phoneshop.web.servlet;

import com.es.phoneshop.model.viewed.ViewedList;
import com.es.phoneshop.service.ViewedListService;
import com.es.phoneshop.service.impl.DefaultViewedListService;
import com.es.phoneshop.web.servlet.ProductListPageServlet;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest {
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

    private final ProductListPageServlet servlet = new ProductListPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        servlet.doGet(request, response);

        verify(request).getParameter("query");
        verify(request).getParameter("sort");
        verify(request).getParameter("order");

        verify(request).setAttribute(eq("products"), anyList());
        verify(request).setAttribute(eq("viewedList"), any(ViewedList.class));

        verify(request).getRequestDispatcher(anyString());
        verify(requestDispatcher).forward(request, response);
    }
}