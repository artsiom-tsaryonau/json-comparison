package com.assignment.diff.jsoncomparison.exception;

import java.util.Set;

/**
 * Exception for cases where there was an attempt to perform comparison where one or both sides are missing.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/19/2019
 *
 * @author Artsiom Tsaryonau
 */
public class NotCompletedComparisonException extends RuntimeException {
    private static final String MESSAGE = "Cannot perform comparison %s as %s are missing";

    /**
     * Constructor.
     * @param comparisonId comparison id
     * @param missingSides missing left or/and right sides of comparison
     */
    public NotCompletedComparisonException(String comparisonId, Set<String> missingSides) {
        super(String.format(MESSAGE, comparisonId, missingSides));
    }
}
