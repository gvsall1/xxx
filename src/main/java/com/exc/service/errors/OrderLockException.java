package com.exc.service.errors;

public class OrderLockException extends LockException {
    public OrderLockException(String s) {
        super(s);
    }
}
