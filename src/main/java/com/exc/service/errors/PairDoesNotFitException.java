package com.exc.service.errors;

public class PairDoesNotFitException extends RuntimeException {
    public PairDoesNotFitException(String s) {
        super(s);
    }
}
