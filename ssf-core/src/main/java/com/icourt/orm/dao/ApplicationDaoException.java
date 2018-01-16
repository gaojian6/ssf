package com.icourt.orm.dao;

/**
 * @author june
 * 2015年07月27日 17:01
 */
public class ApplicationDaoException extends RuntimeException {

    public ApplicationDaoException(String message) {
        super(message);
    }

    public ApplicationDaoException(Throwable cause) {
        super(cause);
    }

    public ApplicationDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
