package com.exc.service.errors;

/**
 * Happen when unknown use case happen during order execution
 */
public class OrderPairExecutionException extends RuntimeException {
    public OrderPairExecutionException(String s) {
        super(s);
    }
}
