package com.es.phoneshop.dao;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.exception.ProductNotFoundException;
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

public class GenericArrayListDaoTest {
    private static ProductDao productDao;
    private static Currency usd;

    @Mock
    Product product;

    @Rule
    public ExpectedException ex = ExpectedException.none();

    @BeforeClass
    public static void setup() {

        usd = Currency.getInstance("USD");
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
    }

    @Test
    public void testGetPresentProduct() {
        Product product = productDao.getProduct(13L);
        assertEquals(13L, (long) product.getId());
    }

    @Test
    public void testGetInvalidProduct() {
        ex.expect(ProductNotFoundException.class);
        productDao.getProduct(14L);
        ex = ExpectedException.none();
    }

    @Test
    public void testGetProductWithNullId() {
        ex.expect(IllegalArgumentException.class);
        productDao.getProduct(null);
        ex = ExpectedException.none();
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
        when(product.getId()).thenReturn(100L);
        ex.expect(ProductNotFoundException.class);
        productDao.save(product);
        ex = ExpectedException.none();
    }

    @Test
    public void testSaveProductWithSameId() throws ProductNotFoundException {
        when(product.getId()).thenReturn(10L);
        when(product.getCode()).thenReturn("test-product");
        productDao.save(product);
        String codeInDao = productDao.getProduct(product.getId()).getCode();
        assertEquals(codeInDao, product.getCode());
    }

    @Test
    public void testDeletePresentProduct() throws ProductNotFoundException {
        productDao.delete(13L);
        ex.expect(ProductNotFoundException.class);
        productDao.getProduct(13L);
        ex = ExpectedException.none();
    }

    @Test
    public void testDeleteInvalidProduct() {
        int prevSize = productDao.findProducts("", SortField.DESCRIPTION, SortOrder.ASC).size();
        productDao.delete(100L);
        int curSize = productDao.findProducts("", SortField.DESCRIPTION, SortOrder.ASC).size();
        assertEquals(prevSize, curSize);
    }

}