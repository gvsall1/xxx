package com.exc.service.errors;

/**
 * happen during lock in ram
 */
public abstract class LockException extends RuntimeException {
    public LockException() {
    }

    public LockException(String s) {
        super(s);
    }

    public LockException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public LockException(Throwable throwable) {
        super(throwable);
    }

    public LockException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
