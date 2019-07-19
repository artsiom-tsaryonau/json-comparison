package com.assignment.diff.jsoncomparison.api;

import com.assignment.diff.jsoncomparison.JsonComparisonResult;

/**
 * Service for comparison of JSON.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/19/2019
 *
 * @author Artsiom Tsaryonau
 */
public interface IJsonComparisonResultService {
    /**
     * Triggers the action of comparison and return the result.
     *
     * Ideally, there supposed to be a separate service that would perform comparison
     * on, for example, timely basis using scheduling. I decided to use simpler approach
     * and perform comparison on demand.
     *
     * @param comparisonId comparison id
     * @return comparison result
     */
    JsonComparisonResult getOrPerformComparison(String comparisonId);
}
