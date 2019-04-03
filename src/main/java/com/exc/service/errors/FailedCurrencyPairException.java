package com.exc.service.errors;

/**
 * happen when OrderPairDTO has wrong fields.
 */
public class FailedCurrencyPairException extends RuntimeException {
    public FailedCurrencyPairException(String s) {
        super(s);
    }
}
