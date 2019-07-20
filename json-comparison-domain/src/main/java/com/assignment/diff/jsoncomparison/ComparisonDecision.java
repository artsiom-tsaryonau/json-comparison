package com.assignment.diff.jsoncomparison;

/**
 * Comparison decision.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/19/2019
 *
 * @author Artsiom Tsaryonau
 */
public enum ComparisonDecision {
    /** Same JSON documents. */
    SAME("JSON binary data is the same"),
    /** JSON documents have different size/length. */
    DIFFERENT_SIZE("JSON binary data has different size"),
    /** JSON documents are different. */
    DIFFERENT("JSON binary data is different"),
    /** Not compared yet. */
    NONE("JSON binary data is not compared yet");

    private String message;

    ComparisonDecision(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
