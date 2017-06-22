package com.lexue.exception;

/**
 * Created by danielmiao on 2017/5/9.
 * Version: 1.0.0
 */
public class LoginInOtherPlaceException extends Exception {
    public LoginInOtherPlaceException() {
    }

    public LoginInOtherPlaceException(String message) {
        super(message);
    }

    public LoginInOtherPlaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginInOtherPlaceException(Throwable cause) {
        super(cause);
    }

    public LoginInOtherPlaceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
