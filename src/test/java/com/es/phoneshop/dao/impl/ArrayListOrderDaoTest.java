package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.product.Product;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)

public class ArrayListOrderDaoTest {
    private static OrderDao orderDao;

    @Mock
    Order order;

    @Rule
    public ExpectedException ex = ExpectedException.none();

    @BeforeClass
    public static void setup() {
        orderDao = ArrayListOrderDao.getInstance();
        Currency usd = Currency.getInstance("USD");
    }

    @Test
    public void testGetSavedOrder() {
        when(order.getId()).thenReturn(null);
        when(order.getSecureId()).thenReturn("0000-0000-0000-0000");
        orderDao.save(order);
        assertEquals(order, orderDao.getOrderBySecureId("0000-0000-0000-0000"));
    }

    @Test
    public void testGetNullSecureId() {
        ex.expect(IllegalArgumentException.class);
        orderDao.getOrderBySecureId(null);
        ex = ExpectedException.none();
    }

    @Test
    public void testSaveOrder() {
        when(order.getId()).thenReturn(100L);
        ex.expect(OrderNotFoundException.class);
        orderDao.save(order);
        ex = ExpectedException.none();
    }



//    @Test
//    public void testGetInvalidProduct() {
//        ex.expect(ProductNotFoundException.class);
//        productDao.getProduct(14L);
//        ex = ExpectedException.none();
//    }
//
//    @Test
//    public void testGetProductWithNullId() {
//        ex.expect(IllegalArgumentException.class);
//        productDao.getProduct(null);
//        ex = ExpectedException.none();
//    }
//
//    @Test
//    public void testSaveProductWithoutId() throws ProductNotFoundException {
//        Product productWithoutId = new Product("test-product", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
//        productDao.save(productWithoutId);
//
//        assertTrue(productWithoutId.getId() >= 0);
//        Product result = productDao.getProduct(productWithoutId.getId());
//        assertNotNull(result);
//        assertEquals("test-product", result.getCode());
//    }
//
//    @Test
//    public void testSaveProductWithWrongId() throws ProductNotFoundException {
//        when(product.getId()).thenReturn(100L);
//        ex.expect(ProductNotFoundException.class);
//        productDao.save(product);
//        ex = ExpectedException.none();
//    }
//
//    @Test
//    public void testSaveProductWithSameId() throws ProductNotFoundException {
//        when(product.getId()).thenReturn(10L);
//        when(product.getCode()).thenReturn("test-product");
//        productDao.save(product);
//        String codeInDao = productDao.getProduct(product.getId()).getCode();
//        assertEquals(codeInDao, product.getCode());
//    }
//
//    @Test
//    public void testDeletePresentProduct() throws ProductNotFoundException {
//        productDao.delete(13L);
//        ex.expect(ProductNotFoundException.class);
//        productDao.getProduct(13L);
//        ex = ExpectedException.none();
//    }
//
//    @Test
//    public void testDeleteInvalidProduct() {
//        int prevSize = productDao.findProducts("", SortField.DESCRIPTION, SortOrder.ASC).size();
//        productDao.delete(100L);
//        int curSize = productDao.findProducts("", SortField.DESCRIPTION, SortOrder.ASC).size();
//        assertEquals(prevSize, curSize);
//    }
}