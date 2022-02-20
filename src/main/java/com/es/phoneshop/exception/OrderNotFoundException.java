package com.es.phoneshop.exception;

public class OrderNotFoundException extends RuntimeException{
    private long orderId;

    public OrderNotFoundException(Long id) {
        super();
        this.orderId = id;
    }

    public OrderNotFoundException(Long id, String message) {
        super(message);
        this.orderId = id;
    }

    public OrderNotFoundException(String message) {
        super(message);
    }

    public long getOrderId() {
        return orderId;
    }
}
