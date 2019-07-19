package com.assignment.diff.jsoncomparison;

import com.assignment.diff.jsoncomparison.api.IJsonComparisonStoringService;
import com.assignment.diff.jsoncomparison.exception.NotUpdatableCompleteComparisonException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (repository.existsById(comparisonId)) {
            JsonComparisonResult stored = repository.getOne(comparisonId);
            if (ComparisonDecision.NONE == stored.getDecision()) {
                stored.setLeftSide(json);
                repository.save(stored);
            } else {
                throw new NotUpdatableCompleteComparisonException(comparisonId);
            }
        } else {
            JsonComparisonResult newResult = new JsonComparisonResult();
            newResult.setDecision(ComparisonDecision.NONE);
            newResult.setComparisonId(comparisonId);
            newResult.setLeftSide(json);
            repository.save(newResult);
        }
    }

    @Override
    public void updateOrCreateRightSide(String comparisonId, String json) {
        if (repository.existsById(comparisonId)) {
            JsonComparisonResult stored = repository.getOne(comparisonId);
            if (ComparisonDecision.NONE == stored.getDecision()) {
                stored.setRightSide(json);
                repository.save(stored);
            } else {
                throw new NotUpdatableCompleteComparisonException(comparisonId);
            }
        } else {
            JsonComparisonResult newResult = new JsonComparisonResult();
            newResult.setDecision(ComparisonDecision.NONE);
            newResult.setComparisonId(comparisonId);
            newResult.setRightSide(json);
            repository.save(newResult);
        }
    }
}
