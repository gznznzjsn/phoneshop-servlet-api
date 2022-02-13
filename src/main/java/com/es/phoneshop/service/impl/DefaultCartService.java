package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
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
    public Cart getCart(HttpServletRequest request) {
        HttpSession session = request.getSession();
        synchronized (session) {
            Cart cart = (Cart) session.getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                session.setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
            }
            return cart;
        }
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        synchronized (cart) {
            Optional<CartItem> sameItem = cart.getItems()
                    .stream()
                    .filter(cartItem -> productId.equals(cartItem.getProduct().getId()))
                    .findAny();
            if (sameItem.isPresent()) {
                int addedQuantity = sameItem.get().getQuantity();
                int totalQuantity = quantity + addedQuantity;
                checkStock(sameItem.get().getProduct(), addedQuantity, quantity);
                sameItem.get().setQuantity(totalQuantity);
            } else {
                Product product = productDao.getProduct(productId);
                checkStock(product, 0, quantity);
                cart.getItems().add(new CartItem(product, quantity));
            }
            recalculateCart(cart);
        }
    }

    @Override
    public void update(Cart cart, Long productId, int quantity) throws OutOfStockException {
        synchronized (cart) {
            if (quantity == 0) {
                delete(cart, productId);
                return;
            }
            Optional<CartItem> optional = cart.getItems()
                    .stream()
                    .filter(cartItem -> productId.equals(cartItem.getProduct().getId())).findAny();
            Product product = optional
                    .orElseThrow(() -> new ProductNotFoundException(productId, "No such id in the cart"))
                    .getProduct();
            checkStock(product, 0, quantity);
            optional.get()
                    .setQuantity(quantity);
            recalculateCart(cart);
        }
    }

    private void checkStock(Product product, int addedQuantity, int quantity) throws OutOfStockException {
        int totalQuantity = quantity + addedQuantity;
        if (product.getStock() < totalQuantity) {
            throw new OutOfStockException(product, quantity, product.getStock() - addedQuantity);
        }
    }

    @Override
    public void delete(Cart cart, Long productId) {
        synchronized (cart) {
            cart.getItems().removeIf(cartItem -> productId.equals(cartItem.getProduct().getId()));
            recalculateCart(cart);
        }
    }

    private void recalculateCart(Cart cart) {
        BigDecimal totalCost = cart.getItems().stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalQuantity = cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
        cart.setTotalCost(totalCost);
        cart.setTotalQuantity(totalQuantity);
    }
}
