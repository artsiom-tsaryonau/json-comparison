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
    SAME("JSON documents are the same"),
    /** JSON documents have different size/length. */
    DIFFERENT_SIZE("JSON documents have different size"),
    /** JSON documents are different. */
    DIFFERENT("JSON documents are different"),
    /** Not compared yet. */
    NONE("JSON documents are not compared yet");

    private String message;

    ComparisonDecision(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
