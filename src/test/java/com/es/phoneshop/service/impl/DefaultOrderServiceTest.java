package com.es.phoneshop.service.impl;

import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultOrderServiceTest {
    private final CartService cartService = DefaultCartService.getInstance();
    private final OrderService orderService = DefaultOrderService.getInstance();

    @Mock
    Order order;



    @Mock
    private ServletContextEvent servletContextEvent;

    @Mock
    private ServletContext servletContext;

    @Mock
    private CartItem cartItem;

    @Mock
    private Cart cart;
    @Rule
    public ExpectedException ex = ExpectedException.none();

    @Before
    public void setup() {
        DemoDataServletContextListener contextListener = new DemoDataServletContextListener();
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);
        when(servletContext.getInitParameter("insertDemoData")).thenReturn("true");
        contextListener.contextInitialized(servletContextEvent);
    }
    @Test
    public void testGetOrder() throws OutOfStockException {
        Cart cart = new Cart();
        cartService.add(cart,1L,1);
        cartService.add(cart,3L,2);
        cartService.add(cart,4L,3);
        Order tmpOrder = orderService.getOrder(cart);
        assertEquals(tmpOrder.getItems().size(),cart.getItems().size());
        for(int i=0;i<tmpOrder.getItems().size();i++){
                assertEquals(tmpOrder.getItems().get(i),cart.getItems().get(i));
        }
        assertNotSame(tmpOrder.getItems(), cart.getItems());
    }

    @Test
    public void testGetOrderWithUncloneable() throws CloneNotSupportedException {
        when(cart.getItems()).thenReturn(new ArrayList<>(Collections.singletonList(cartItem)));
        when(cartItem.clone()).thenThrow(new CloneNotSupportedException());
        ex.expect(RuntimeException.class);
        orderService.getOrder(cart);
        ex = ExpectedException.none();
    }

    @Test
    public void testGetPaymentMethods() {
        assertEquals(orderService.getPaymentMethods(), new ArrayList<>(Arrays.asList(PaymentMethod.CASH, PaymentMethod.CREDIT_CARD)));
    }

    @Test
    public void testPlaceOrder() {
        when(order.getId()).thenReturn(null);
        orderService.placeOrder(order);
        verify(order).setSecureId(anyString());
        assertEquals(orderService.getPaymentMethods(), new ArrayList<>(Arrays.asList(PaymentMethod.CASH, PaymentMethod.CREDIT_CARD)));
    }



}