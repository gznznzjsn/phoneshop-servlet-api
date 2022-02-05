package com.es.phoneshop.model.cart;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";

    private final ProductDao productDao;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    private static CartService instance;

    public static CartService getInstance() {
        if (instance == null) {
            synchronized (DefaultCartService.class) {
                if (instance == null) {
                    instance = new DefaultCartService();
                }
            }
        }
        return instance;
    }

    @Override
    public synchronized Cart getCart(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
        }
        return cart;
    }

    @Override
    public synchronized void add(Cart cart, Long productId, int quantity) throws ProductNotFoundException, OutOfStockException {
        Product product = productDao.getProduct(productId);
        Optional<CartItem> sameItem = cart.getItems()
                .stream()
                .filter(cartItem -> product.equals(cartItem.getProduct()))
                .findAny();
        if (sameItem.isPresent()) {
            int addedQuantity = sameItem.get().getQuantity();
            int totalQuantity = quantity + addedQuantity;
            if (product.getStock() < totalQuantity) {
                throw new OutOfStockException(product, quantity, product.getStock() - addedQuantity);
            }
            int sameItemId = cart.getItems().indexOf(sameItem.get());
            cart.getItems().get(sameItemId).setQuantity(totalQuantity);
        } else {
            if (product.getStock() < quantity) {
                throw new OutOfStockException(product, quantity, product.getStock());
            }
            cart.getItems().add(new CartItem(product, quantity));
        }
    }
}
