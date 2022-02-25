package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.viewed.ViewedList;
import com.es.phoneshop.service.ViewedListService;
import com.es.phoneshop.web.listener.DemoDataServletContextListener;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultViewedListServiceTest {
    private final ViewedListService viewedListService = DefaultViewedListService.getInstance();
    private final ProductDao productDao = ArrayListProductDao.getInstance();

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletRequest request;

    @Mock
    private ServletContextEvent servletContextEvent;

    @Mock
    private ServletContext servletContext;

    @Rule
    public ExpectedException ex = ExpectedException.none();

    @Before
    public void setup() {
        DemoDataServletContextListener contextListener = new DemoDataServletContextListener();
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);
        when(servletContext.getInitParameter("insertDemoData")).thenReturn("true");
        contextListener.contextInitialized(servletContextEvent);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testGetNullViewedList() {
        viewedListService.getViewedList(request);
        verify(session).setAttribute(anyString(), any(ViewedList.class));
    }

    @Test
    public void testGetNotNullViewedList() {
        ViewedList viewedList = new ViewedList();
        when(session.getAttribute(anyString())).thenReturn(viewedList);
        ViewedList newViewedList = viewedListService.getViewedList(request);
        assertSame(viewedList, newViewedList);
    }


    @Test
    public void testUpdateWithViewedProduct() {
        ViewedList viewedList = new ViewedList();
        viewedList.setCurrentVersionList(Collections.singletonList(productDao.getProduct(1L)));
        viewedListService.update(viewedList, 1L);
        assertEquals(viewedList.getCurrentVersionList(), viewedList.getPreviousVersionList());
    }

    @Test
    public void testUpdateWithNewProduct() {
        ViewedList viewedList = new ViewedList();
        viewedListService.update(viewedList, 1L);
        assertEquals(1, viewedList.getCurrentVersionList().size());
        assertEquals(0, viewedList.getPreviousVersionList().size());
        assertEquals(productDao.getProduct(1L), viewedList.getCurrentVersionList().get(0));
    }

    @Test
    public void testUpdateFulfilledViewedList() {
        ViewedList viewedList = new ViewedList();
        viewedListService.update(viewedList, 1L);
        viewedListService.update(viewedList, 3L);
        viewedListService.update(viewedList, 4L);
        assertEquals(productDao.getProduct(1L), viewedList.getCurrentVersionList().get(0));
        assertEquals(productDao.getProduct(3L), viewedList.getCurrentVersionList().get(1));
        assertEquals(productDao.getProduct(4L), viewedList.getCurrentVersionList().get(2));
        List<Product> previousList = new ArrayList<>(viewedList.getCurrentVersionList()) ;
        viewedListService.update(viewedList, 5L);
        assertEquals(productDao.getProduct(3L), viewedList.getCurrentVersionList().get(0));
        assertEquals(productDao.getProduct(4L), viewedList.getCurrentVersionList().get(1));
        assertEquals(productDao.getProduct(5L), viewedList.getCurrentVersionList().get(2));
        assertEquals(3, viewedList.getCurrentVersionList().size());
        assertEquals(viewedList.getPreviousVersionList(), previousList);


    }


}