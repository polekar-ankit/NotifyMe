package com.gipl.notifyme.exceptions;

/**
 * Exception throw by the application when a User search can't return a valid result.
 */
public class CustomException extends Exception {
    public CustomException() {
        super();
    }


    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomException(Throwable cause) {
        super(cause);
    }
}
