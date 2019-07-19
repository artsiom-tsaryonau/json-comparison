package com.assignment.diff.jsoncomparison.exception;

/**
 * Exception for cases of updating the completed comparison.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/19/2019
 *
 * @author Artsiom Tsaryonau
 */
public class NotUpdatableCompleteComparisonException extends RuntimeException {
    private static final String MESSAGE = "Cannot update completed comparison with id [%s]";

    /**
     * Constructor.
     * @param comparisonId comparison
     */
    public NotUpdatableCompleteComparisonException(String comparisonId) {
        super(String.format(MESSAGE, comparisonId));
    }
}
