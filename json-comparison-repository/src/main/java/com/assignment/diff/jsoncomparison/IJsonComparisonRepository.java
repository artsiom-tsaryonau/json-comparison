package com.assignment.diff.jsoncomparison;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for storing JSON comparison data.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/19/2019
 *
 * @author Artsiom Tsaryonau
 */
public interface IJsonComparisonRepository extends JpaRepository<JsonComparisonResult, String> {
}
