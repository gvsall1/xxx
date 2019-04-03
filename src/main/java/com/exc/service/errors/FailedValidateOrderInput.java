package com.exc.service.errors;

public class FailedValidateOrderInput extends RuntimeException {
    public FailedValidateOrderInput(String s) {
        super(s);
    }
}
