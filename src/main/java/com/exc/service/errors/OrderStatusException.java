package com.exc.service.errors;


public class OrderStatusException extends RuntimeException {
    public OrderStatusException(String s) {
        super(s);
    }
}
