package com.icourt.quartz;

/**
 * @author june
 */
public class QuartzException extends RuntimeException {

    public QuartzException(String message) {
        super(message);
    }

    public QuartzException(Throwable cause) {
        super(cause);
    }

    public QuartzException(String message, Throwable cause) {
        super(message, cause);
    }
}
