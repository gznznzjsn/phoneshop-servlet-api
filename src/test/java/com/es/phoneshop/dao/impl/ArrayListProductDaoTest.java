package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.enums.SortField;
import com.es.phoneshop.dao.enums.SortOrder;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ArrayListProductDaoTest {
    private ProductDao productDao;
    private Currency usd;

    @Mock
    Product product;

    @Rule
    public ExpectedException ex = ExpectedException.none();

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
        productDao.save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        productDao.save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        productDao.save(new Product("iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        productDao.save(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        productDao.save(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        productDao.save(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        productDao.save(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        productDao.save(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        productDao.save(new Product("palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        productDao.save(new Product("simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        productDao.save(new Product("simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        productDao.save(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));

        usd = Currency.getInstance("USD");

        given(product.getId()).willReturn(100L);
        given(product.getPrice()).willReturn(BigDecimal.valueOf(100));
        given(product.getStock()).willReturn(1);
    }

    @Test
    public void testGetPresentProduct() throws ProductNotFoundException {
        Product product = productDao.getProduct(13L);
        assertEquals(13L, (long) product.getId());
    }

    @Test
    public void testGetInvalidProduct() throws ProductNotFoundException {
        ex.expect(ProductNotFoundException.class);
        Product product = productDao.getProduct(14L);
        ex = ExpectedException.none();
        assertNull(product);
    }

    @Test
    public void testGetProductWithNullId() throws ProductNotFoundException {
        ex.expect(ProductNotFoundException.class);
        Product product = productDao.getProduct(null);
        ex = ExpectedException.none();
        assertNull(product);
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts("", SortField.DESCRIPTION, SortOrder.ASC).isEmpty());
    }

    @Test
    public void testSaveProductWithoutId() throws ProductNotFoundException {
        Product productWithoutId = new Product("test-product", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(productWithoutId);

        assertTrue(productWithoutId.getId() >= 0);
        Product result = productDao.getProduct(productWithoutId.getId());
        assertNotNull(result);
        assertEquals("test-product", result.getCode());
    }

    @Test
    public void testSaveProductWithWrongId() throws ProductNotFoundException {
        ex.expect(ProductNotFoundException.class);
        productDao.save(product);
        Product pr = productDao.getProduct(product.getId());
        ex = ExpectedException.none();
        assertNull(pr);
    }

    @Test
    public void testUpdateProduct() throws ProductNotFoundException {
        given(product.getId()).willReturn(5L);
        given(product.getCode()).willReturn("test-code");
        productDao.save(product);
        assertEquals(productDao.getProduct(5L).getCode(), product.getCode());
    }

    @Test
    public void testDeletePresentProduct() throws ProductNotFoundException {
        productDao.delete(13L);
        ex.expect(ProductNotFoundException.class);
        productDao.getProduct(13L);
        ex = ExpectedException.none();
    }

    @Test
    public void testDeleteInvalidProduct() throws ProductNotFoundException {
        int prevSize = productDao.findProducts("", SortField.DESCRIPTION, SortOrder.ASC).size();
        productDao.delete(100L);
        int curSize = productDao.findProducts("", SortField.DESCRIPTION, SortOrder.ASC).size();
        assertEquals(prevSize, curSize);
    }

}
