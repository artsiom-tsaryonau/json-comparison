package com.assignment.diff.jsoncomparison;

import com.assignment.diff.jsoncomparison.api.IJsonComparisonStoringService;
import com.assignment.diff.jsoncomparison.exception.NotUpdatableCompleteComparisonException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * Implementation of {@link IJsonComparisonStoringService}.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/19/2019
 *
 * @author Artsiom Tsaryonau
 */
@Service
@Transactional
public class JsonComparisonStoringService implements IJsonComparisonStoringService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonComparisonStoringService.class);
    private final IJsonComparisonRepository repository;

    /**
     * Constructor.
     * @param repository repository
     */
    @Autowired
    public JsonComparisonStoringService(IJsonComparisonRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<JsonComparisonResult> updateOrCreateLeftSide(String comparisonId, String json) {
        return updateComparisonSide(comparisonId, storedComparisonResult -> {
            storedComparisonResult.setLeftSide(json);
            return storedComparisonResult;
        });
    }

    @Override
    public Mono<JsonComparisonResult> updateOrCreateRightSide(String comparisonId, String json) {
        return updateComparisonSide(comparisonId, storedComparisonResult -> {
            storedComparisonResult.setRightSide(json);
            return storedComparisonResult;
        });
    }

    private Mono<JsonComparisonResult> updateComparisonSide(String comparisonId, Function<JsonComparisonResult,
            JsonComparisonResult> updateSide) {
        return repository.findById(comparisonId)
            .defaultIfEmpty(createResult(comparisonId))
            // if not NONE - it means it was found and completed
            .filter(result -> ComparisonDecision.NONE == result.getDecision())
            .switchIfEmpty(Mono.error(new NotUpdatableCompleteComparisonException(comparisonId)))
            .map(updateSide)
            .flatMap(repository::save);

    }

    private JsonComparisonResult createResult(String comparisonId) {
        LOGGER.info("Creating new comparison result: {}.", comparisonId);
        var newResult = new JsonComparisonResult();
        newResult.setDecision(ComparisonDecision.NONE);
        newResult.setComparisonId(comparisonId);
        newResult.setIsNew(true);
        return newResult;
    }
}
