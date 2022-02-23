package com.es.phoneshop.exception;

public class OrderNotFoundException extends ItemNotFoundException{
    private String secureId;


    public OrderNotFoundException(Long id) {
        super(id);
    }

    public OrderNotFoundException(Long id, String message) {
        super(id,message);
    }

    public OrderNotFoundException(String secureId, String message) {
        super(message);
        this.secureId = secureId;
    }


    public OrderNotFoundException(String message) {
        super(message);
    }

    public String getSecureId() {
        return secureId;
    }
}
