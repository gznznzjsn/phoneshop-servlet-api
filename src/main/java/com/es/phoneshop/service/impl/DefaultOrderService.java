package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.OrderService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultOrderService implements OrderService {
    private final OrderDao orderDao;

    private DefaultOrderService() {
        orderDao = ArrayListOrderDao.getInstance();
    }

    private static OrderService instance;

    public static OrderService getInstance() {
        if (instance == null) {
            synchronized (DefaultOrderService.class) {
                if (instance == null) {
                    instance = new DefaultOrderService();
                }
            }
        }
        return instance;
    }

    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();
        order.setCurrency(cart.getCurrency());
        order.setItems(cart.getItems().stream().map(cartItem -> {
            try {
                return (CartItem) cartItem.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Failure cloning cartItem");
            }
        }).collect(Collectors.toList()));
        order.setSubtotal(cart.getTotalCost());
        order.setDeliveryCost(calculateDeliveryCost());
        order.setTotalCost(order.getSubtotal().add(order.getDeliveryCost()));
        return order;
    }

    private BigDecimal calculateDeliveryCost(){
        return new BigDecimal(5);
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values()) ;
    }

    @Override
    public void placeOrder(Order order) {
        order.setSecureId(String.valueOf(UUID.randomUUID()));
        orderDao.save(order);
    }
}
