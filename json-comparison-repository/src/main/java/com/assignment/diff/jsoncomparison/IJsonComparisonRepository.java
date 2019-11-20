package com.assignment.diff.jsoncomparison;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Repository for storing JSON comparison data.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/19/2019
 *
 * @author Artsiom Tsaryonau
 */
public interface IJsonComparisonRepository extends ReactiveCrudRepository<JsonComparisonResult, String> {
}
