package com.assignment.diff.jsoncomparison.api;

/**
 * Service for storing and reading comparison result.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/19/2019
 *
 * @author Artsiom Tsaryonau
 */
public interface IJsonComparisonStoringService {
    /**
     * Stores left side of JSON comparison.
     * @param comparisonId comparison id
     * @param json json to store
     */
    void updateOrCreateLeftSide(String comparisonId, String json);
    /**
     * Store right side of JSON comparison.
     * @param comparisonId comparison id
     * @param json json to store
     */
    void updateOrCreateRightSide(String comparisonId, String json);
}
