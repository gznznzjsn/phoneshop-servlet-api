package com.es.phoneshop.exception;

public class ProductNotFoundException extends RuntimeException{
    private long productId;

    public ProductNotFoundException(Long id) {
        super();
        this.productId = id;
    }

    public ProductNotFoundException(Long id, String message) {
        super(message);
        this.productId = id;
    }

    public ProductNotFoundException( String message) {
        super(message);
    }

    public long getProductId() {
        return productId;
    }
}
