package com.assignment.diff.jsoncomparison.exception;

/**
 * Exception for cases of invalid JSON.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/19/2019
 *
 * @author Artsiom Tsaryonau
 */
public class InvalidJsonException extends RuntimeException {
    private static final String MESSAGE = "Invalid JSON";

    /**
     * Constructor.
     * @param e cause exception
     */
    public InvalidJsonException(Exception e) {
        super(MESSAGE, e);
    }
}
