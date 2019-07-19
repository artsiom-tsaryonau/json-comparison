package com.assignment.diff.jsoncomparison.exception;

/**
 * Exception for cases when no comparison found.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/19/2019
 *
 * @author Artsiom Tsaryonau
 */
public class NoComparisonFoundException extends RuntimeException {
    private static final String MESSAGE = "No comparison with id [%s] found";

    /**
     * Constructor.
     * @param comparisonId cause exception
     */
    public NoComparisonFoundException(String comparisonId) {
        super(String.format(MESSAGE, comparisonId));
    }
}
