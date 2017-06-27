package com.lexue.exception;

/**
 * Created by danielmiao on 2017/5/9.
 * Version: 1.0.0
 */
public class SessionErrorException extends Exception {
    public SessionErrorException() {
    }

    public SessionErrorException(String message) {
        super(message);
    }

    public SessionErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionErrorException(Throwable cause) {
        super(cause);
    }

    public SessionErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
