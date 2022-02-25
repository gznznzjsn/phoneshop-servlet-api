package com.es.phoneshop.exception;

public class ItemNotFoundException extends RuntimeException{
    private long id;

    public ItemNotFoundException(Long id) {
        super();
        this.id = id;
    }

    public ItemNotFoundException(Long id, String message) {
        super(message);
        this.id = id;
    }


    public ItemNotFoundException(String message) {
        super(message);
    }

    public long getId() {
        return id;
    }

}
