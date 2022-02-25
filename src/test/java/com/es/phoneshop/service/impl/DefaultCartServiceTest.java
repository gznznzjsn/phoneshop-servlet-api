package com.es.phoneshop.service.impl;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
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
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCartServiceTest {
    private final CartService cartService = DefaultCartService.getInstance();

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
    public void testGetNullCart() {
        cartService.getCart(request);
        verify(session).setAttribute(anyString(), any(Cart.class));
    }

    @Test
    public void testGetNotNullCart() {
        Cart cart = new Cart();
        when(session.getAttribute(anyString())).thenReturn(cart);
        Cart newCart = cartService.getCart(request);
        assertSame(cart, newCart);
    }


    @Test
    public void testAddZeroQuantity() throws OutOfStockException {
        Cart cart = new Cart();
        cartService.add(cart, 1L, 0);
        assertEquals(0, cart.getItems().size());
        assertEquals(BigDecimal.valueOf(0),cart.getTotalCost());
        assertEquals(0,cart.getTotalQuantity());
    }

    @Test
    public void testAddNewItem() throws OutOfStockException {
        Cart cart = new Cart();
        cartService.add(cart, 1L, 1);
        cartService.add(cart, 3L, 1);
        assertEquals(Long.valueOf(1L), cart.getItems().get(0).getProduct().getId());
        assertEquals(Long.valueOf(3L), cart.getItems().get(1).getProduct().getId());
        assertEquals(BigDecimal.valueOf(5330),cart.getTotalCost());
        assertEquals(2,cart.getTotalQuantity());
    }

    @Test
    public void testAddSameItem() throws OutOfStockException {
        Cart cart = new Cart();
        cartService.add(cart, 1L, 1);
        cartService.add(cart, 1L, 2);
        assertEquals(3, cart.getItems().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(5030*3),cart.getTotalCost());
        assertEquals(3,cart.getTotalQuantity());
    }

    @Test
    public void testUpdateWithZeroQuantity() throws OutOfStockException {
        Cart cart = new Cart();
        cartService.add(cart, 1L, 1);
        assertEquals(1, cart.getItems().size());
        cartService.update(cart, 1L, 0);
        assertEquals(0, cart.getItems().size());
        assertEquals(BigDecimal.valueOf(0),cart.getTotalCost());
        assertEquals(0,cart.getTotalQuantity());
    }

    @Test
    public void testUpdatePresentItem() throws OutOfStockException {
        Cart cart = new Cart();
        cartService.add(cart, 1L, 1);
        assertEquals(1, cart.getItems().size());
        assertEquals(1, cart.getItems().get(0).getQuantity());
        cartService.update(cart, 1L, 2);
        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(10060),cart.getTotalCost());
        assertEquals(2,cart.getTotalQuantity());
    }

    @Test
    public void testUpdateInvalidItem() throws OutOfStockException {
        Cart cart = new Cart();
        ex.expect(ProductNotFoundException.class);
        cartService.update(cart, 100L, 2);
        ex = ExpectedException.none();
    }

    @Test
    public void testCheckStock() throws OutOfStockException {
        Cart cart = new Cart();
        ex.expect(OutOfStockException.class);
        cartService.add(cart, 1L, 1000);
        ex = ExpectedException.none();
    }

    @Test
    public void testDeleteItem() throws OutOfStockException {
        Cart cart = new Cart();
        cartService.add(cart, 1L, 1);
        assertEquals(1, cart.getItems().size());
        cartService.delete(cart, 1L);
        assertEquals(0, cart.getItems().size());
        assertEquals(BigDecimal.valueOf(0),cart.getTotalCost());
        assertEquals(0,cart.getTotalQuantity());
    }

    @Test
    public void testDeleteInvalidItem() {
        Cart cart = new Cart();
        ex.expect(ProductNotFoundException.class);
        cartService.delete(cart, 1L);
        ex = ExpectedException.none();
    }

    @Test
    public void testClearCart() throws OutOfStockException {
        Cart cart = new Cart();
        cartService.add(cart,1L,1);
        cartService.add(cart,3L,1);
        cartService.add(cart,4L,1);
        assertEquals(3, cart.getItems().size());
        cartService.clear(cart);
        assertEquals(0, cart.getItems().size());
        assertEquals(BigDecimal.valueOf(0),cart.getTotalCost());
        assertEquals(0,cart.getTotalQuantity());
    }
}