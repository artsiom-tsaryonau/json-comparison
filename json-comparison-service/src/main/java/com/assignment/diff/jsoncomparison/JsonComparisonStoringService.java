package com.assignment.diff.jsoncomparison;

import com.assignment.diff.jsoncomparison.api.IJsonComparisonStoringService;
import com.assignment.diff.jsoncomparison.exception.NotUpdatableCompleteComparisonException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

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
    public void updateOrCreateLeftSide(String comparisonId, String json) {
        updateComparisonSide(comparisonId, storedComparisonResult -> storedComparisonResult.setLeftSide(json));
    }

    @Override
    public void updateOrCreateRightSide(String comparisonId, String json) {
        updateComparisonSide(comparisonId, storedComparisonResult -> storedComparisonResult.setRightSide(json));
    }

    private void updateComparisonSide(String comparisonId, Consumer<JsonComparisonResult> updateSide) {
        if (repository.existsById(comparisonId)) {
            JsonComparisonResult stored = repository.getOne(comparisonId);
            if (ComparisonDecision.NONE == stored.getDecision()) {
                updateSide.accept(stored);
                repository.save(stored);
            } else {
                throw new NotUpdatableCompleteComparisonException(comparisonId);
            }
        } else {
            LOGGER.info("No comparison found with {}. Creating new.", comparisonId);
            JsonComparisonResult newResult = new JsonComparisonResult();
            newResult.setDecision(ComparisonDecision.NONE);
            newResult.setComparisonId(comparisonId);

            updateSide.accept(newResult);

            repository.save(newResult);
        }
    }
}
