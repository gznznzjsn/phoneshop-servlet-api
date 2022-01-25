package com.es.phoneshop.model.product;

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
    Product lastProduct;

    @Mock
    Product product;

    @Rule
    public ExpectedException ex = ExpectedException.none();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        productDao = new ArrayListProductDao();
        usd = Currency.getInstance("USD");

        given(lastProduct.getId()).willReturn(100L);
        given(lastProduct.getPrice()).willReturn(BigDecimal.valueOf(100));
        given(lastProduct.getStock()).willReturn(1);

        given(product.getPrice()).willReturn(BigDecimal.valueOf(100));
        given(product.getStock()).willReturn(1);
    }

    @Test
    public void testGetProductPositive() throws ProductNotFoundException {
        Product product =  productDao.getProduct(13L);
        assertEquals(13L, (long) product.getId());
    }

    @Test
    public void testGetProductNegative() throws ProductNotFoundException {
        ex.expect(ProductNotFoundException.class);
        Product product =  productDao.getProduct(14L);
        ex = ExpectedException.none();
        assertNull(product);
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testSaveProductWithoutId() throws ProductNotFoundException {
        Product product = new Product("test-product", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        assertTrue(product.getId() >= 0);
        Product result =  productDao.getProduct(product.getId());
        assertNotNull(result);
        assertEquals("test-product", result.getCode());
    }

    @Test
    public void testSaveProductWithTooBigId() throws ProductNotFoundException {
        int prevSize = productDao.findProducts().size();
        productDao.save(lastProduct);
        int curSize = productDao.findProducts().size();
        assertEquals(prevSize + 1, curSize);
        assertEquals(productDao.findProducts().get(curSize - 1),lastProduct);
    }

    @Test
    public void testSaveProductWithLessThanMaxIdFound() throws ProductNotFoundException{
        productDao.save(lastProduct);

        given(product.getId()).willReturn(5L);
        given(product.getCode()).willReturn("test-code");
        productDao.save(product);
        assertEquals(productDao.getProduct(5L).getCode(), product.getCode());
    }

    @Test
    public void testSaveProductWithLessThanMaxIdNotFound() throws ProductNotFoundException {
        productDao.save(lastProduct);

        given(product.getId()).willReturn(98L);
        productDao.save(product);

        int index100 = productDao.findProducts().indexOf(lastProduct);
        int index98 = productDao.findProducts().indexOf(product);
        assertTrue(index98 < index100);
    }

    @Test
    public void testDeleteProduct() throws ProductNotFoundException{
        productDao.delete(13L);
        ex.expect(ProductNotFoundException.class);
        productDao.getProduct(13L);
        ex = ExpectedException.none();
    }

}
