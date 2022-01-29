package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.Product;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;


public class ArrayListProductDaoTest
{
    private ProductDao productDao;
    private Currency usd;

    @Mock
    Product product;

    @Rule
    public ExpectedException ex = ExpectedException.none();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        productDao = ArrayListProductDao.getInstance();
        usd = Currency.getInstance("USD");

        given(product.getId()).willReturn(100L);
        given(product.getPrice()).willReturn(BigDecimal.valueOf(100));
        given(product.getStock()).willReturn(1);
    }

    @Test
    public void testGetPresentProduct() throws ProductNotFoundException {
        Product product =  productDao.getProduct(13L);
        assertEquals(13L, (long) product.getId());
    }

    @Test
    public void testGetInvalidProduct() throws ProductNotFoundException {
        ex.expect(ProductNotFoundException.class);
        Product product =  productDao.getProduct(14L);
        ex = ExpectedException.none();
        assertNull(product);
    }

    @Test
    public void testGetProductWithNullId() throws ProductNotFoundException {
        ex.expect(ProductNotFoundException.class);
        Product product =  productDao.getProduct(null);
        ex = ExpectedException.none();
        assertNull(product);
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts("",SortField.description,SortOrder.asc).isEmpty());
    }

    @Test
    public void testSaveProductWithoutId() throws ProductNotFoundException {
        Product productWithoutId = new Product("test-product", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(productWithoutId);

        assertTrue(productWithoutId.getId() >= 0);
        Product result =  productDao.getProduct(productWithoutId.getId());
        assertNotNull(result);
        assertEquals("test-product", result.getCode());
    }

    @Test
    public void testSaveProductWithWrongId() throws ProductNotFoundException {
        ex.expect(ProductNotFoundException.class);
        productDao.save(product);
        Product pr =  productDao.getProduct(product.getId());
        ex = ExpectedException.none();
        assertNull(pr);
    }

    @Test
    public void testUpdateProduct() throws ProductNotFoundException{
        given(product.getId()).willReturn(5L);
        given(product.getCode()).willReturn("test-code");
        productDao.save(product);
        assertEquals(productDao.getProduct(5L).getCode(), product.getCode());
    }

    @Test
    public void testDeletePresentProduct() throws ProductNotFoundException{
        productDao.delete(13L);
        ex.expect(ProductNotFoundException.class);
        productDao.getProduct(13L);
        ex = ExpectedException.none();
    }

    @Test
    public void testDeleteInvalidProduct() throws ProductNotFoundException{
        int prevSize = productDao.findProducts("",SortField.description,SortOrder.asc).size();
        productDao.delete(100L);
        int curSize = productDao.findProducts("",SortField.description,SortOrder.asc).size();
        assertEquals(prevSize,curSize);
    }

}
