package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArrayListProductDaoTest {
    private static ProductDao productDao;

    @Mock
    Product product;

    @Rule
    public ExpectedException ex = ExpectedException.none();


    @BeforeClass
    public static void setup() {
        Currency usd = Currency.getInstance("USD");
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
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts("", SortField.DESCRIPTION, SortOrder.ASC).isEmpty());
    }


    @Test
    public void testContainsAllWords() {
        when(product.getDescription()).thenReturn("Samsung Galaxy 555");
        when(product.getId()).thenReturn(null);
        when(product.getPrice()).thenReturn(new BigDecimal(100));
        when(product.getStock()).thenReturn(1);
        productDao.save(product);
        assertTrue(productDao.findProducts("Sa G", SortField.DESCRIPTION, SortOrder.ASC).contains(product));
    }

    @Test
    public void testDoesNotContainsAllWords() {
        when(product.getDescription()).thenReturn("Galaxy");
        when(product.getId()).thenReturn(null);
        when(product.getPrice()).thenReturn(new BigDecimal(100));
        when(product.getStock()).thenReturn(1);
        productDao.save(product);
        assertFalse(productDao.findProducts("Sa G", SortField.DESCRIPTION, SortOrder.ASC).contains(product));
    }

    @Test
    public void testComparatorOrder() {
        assertEquals(productDao.findProducts("", SortField.DESCRIPTION, SortOrder.ASC).stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(Product::getDescription)))
                .collect(Collectors.toList()), productDao.findProducts("", SortField.DESCRIPTION, SortOrder.DESC));
    }

    @Test
    public void testComparatorNullSort() {
        assertEquals(productDao.findProducts("S", null, null).stream()
                .sorted(Comparator.comparing(product -> product.getDescription().split(" ").length))
                .collect(Collectors.toList()), productDao.findProducts("S", null, null));
    }



}
