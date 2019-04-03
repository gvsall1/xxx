package com.exc.service.errors;

public class OrderPairCancelException extends RuntimeException {
    public OrderPairCancelException(String s) {
        super(s);
    }
}
