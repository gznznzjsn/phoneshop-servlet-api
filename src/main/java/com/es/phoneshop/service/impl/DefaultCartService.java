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
        if (quantity == 0) {
            return;
        }
        synchronized (cart) {
            Optional<CartItem> sameItem = cart.getItems()
                    .stream()
                    .filter(cartItem -> productId.equals(cartItem.getProduct().getId()))
                    .findAny();
            Product product = productDao.getProduct(productId);
            if (sameItem.isPresent()) {
                checkStock(sameItem.get().getProduct(), quantity);
                sameItem.get().setQuantity(sameItem.get().getQuantity() + quantity);
            } else {
                checkStock(product, quantity);
                cart.getItems().add(new CartItem(product, quantity));
            }
            product.setStock(product.getStock() - quantity);
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
            Optional<CartItem> cartItemOptional = cart.getItems()
                    .stream()
                    .filter(cartItem -> productId.equals(cartItem.getProduct().getId())).findAny();
            Product product = cartItemOptional
                    .orElseThrow(() -> new ProductNotFoundException(productId, "No such id in the cart"))
                    .getProduct();
            checkStock(product, quantity);
            Product productToUpdate = productDao.getProduct(productId);
            productToUpdate.setStock(productToUpdate.getStock() + cartItemOptional.get()
                    .getQuantity() - quantity);
            cartItemOptional.get()
                    .setQuantity(quantity);
            recalculateCart(cart);
        }
    }

    private void checkStock(Product product, int quantity) throws OutOfStockException {
        if (product.getStock() < quantity) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
    }

    @Override
    public void delete(Cart cart, Long productId) {
        synchronized (cart) {
            Product productToUpdate = productDao.getProduct(productId);
            int quantity = cart.getItems().stream()
                    .filter(cartItem -> productId.equals(cartItem.getProduct().getId()))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException(productId, "No such id in the cart"))
                    .getQuantity();
            productToUpdate.setStock(productToUpdate.getStock() + quantity);
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
