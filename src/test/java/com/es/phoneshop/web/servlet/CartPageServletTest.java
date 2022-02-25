package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.impl.DefaultCartService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {
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

    @Rule
    public ExpectedException ex = ExpectedException.none();

    private final CartPageServlet servlet = new CartPageServlet();

    @BeforeClass
    public static void setupClass() {
        Currency usd = Currency.getInstance("USD");
        ProductDao productDao = ArrayListProductDao.getInstance();
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

        @Before
    public void setup() throws ServletException {
        servlet.init(config);
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getLocale()).thenReturn(Locale.ROOT);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(request).setAttribute(eq("cart"), any(Cart.class));
        verify(request).getRequestDispatcher("/WEB-INF/pages/cart.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostWrongFormat() throws ServletException, IOException {
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"a","b","c"});
        when(request.getParameterValues("productId")).thenReturn(new String[]{"1","2","3"});
        servlet.doPost(request, response);
        verify(request).setAttribute(eq("errors"), anyMap());
        verify(request).getRequestDispatcher("/WEB-INF/pages/cart.jsp");
    }


    @Test
    public void testDoPostOutOfStock() throws ServletException, IOException, OutOfStockException {
        Cart cart = new Cart();
        when(session.getAttribute(DefaultCartService.class.getName() + ".cart")).thenReturn(cart);
        DefaultCartService.getInstance().add(DefaultCartService.getInstance().getCart(request),3L,1);
        DefaultCartService.getInstance().add(DefaultCartService.getInstance().getCart(request),4L,1);
        DefaultCartService.getInstance().add(DefaultCartService.getInstance().getCart(request),5L,1);
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"100","100","100"});
        when(request.getParameterValues("productId")).thenReturn(new String[]{"3","4","5"});
        servlet.doPost(request, response);
        verify(request).setAttribute(eq("errors"), anyMap());
        verify(request).getRequestDispatcher("/WEB-INF/pages/cart.jsp");
    }


    @Test
    public void testDoPostSuccess() throws ServletException, IOException, OutOfStockException {
        Cart cart = new Cart();
        when(session.getAttribute(DefaultCartService.class.getName() + ".cart")).thenReturn(cart);
        DefaultCartService.getInstance().add(DefaultCartService.getInstance().getCart(request),3L,1);
        DefaultCartService.getInstance().add(DefaultCartService.getInstance().getCart(request),4L,1);
        DefaultCartService.getInstance().add(DefaultCartService.getInstance().getCart(request),5L,1);
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"2","2","2"});
        when(request.getParameterValues("productId")).thenReturn(new String[]{"3","4","5"});
        servlet.doPost(request, response);

        verify(response).sendRedirect(String.format("%s/cart?message=Cart updated successfully", request.getContextPath()));
    }


}