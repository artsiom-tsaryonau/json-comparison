package com.assignment.diff.jsoncomparison.api;

import com.assignment.diff.jsoncomparison.JsonComparisonResult;
import reactor.core.publisher.Mono;

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
     * @return comparison result
     */
    Mono<JsonComparisonResult> updateOrCreateLeftSide(String comparisonId, String json);
    /**
     * Store right side of JSON comparison.
     * @param comparisonId comparison id
     * @param json json to store
     * @return comparison result
     */
    Mono<JsonComparisonResult> updateOrCreateRightSide(String comparisonId, String json);
}
