package com.es.phoneshop.exception;

public class ProductNotFoundException extends ItemNotFoundException{

    public ProductNotFoundException(Long id) {
        super(id);
    }

    public ProductNotFoundException(Long id, String message) {
        super(id,message);
    }

    public ProductNotFoundException( String message) {
        super(message);
    }
}
