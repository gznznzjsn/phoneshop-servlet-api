package com.es.phoneshop.exception;

public class OrderNotFoundException extends RuntimeException{
    private long orderId;
    private String secureId;


    public OrderNotFoundException(Long id) {
        super();
        this.orderId = id;
    }

    public OrderNotFoundException(Long id, String message) {
        super(message);
        this.orderId = id;
    }

    public OrderNotFoundException(String id, String message) {
        super(message);
        this.secureId = id;
    }


    public OrderNotFoundException(String message) {
        super(message);
    }

    public long getOrderId() {
        return orderId;
    }

    public String getSecureId() {
        return secureId;
    }
}
