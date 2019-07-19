package com.assignment.diff.jsoncomparison;

import com.assignment.diff.jsoncomparison.api.IJsonComparisonResultService;
import com.assignment.diff.jsoncomparison.exception.NoComparisonFoundException;
import com.assignment.diff.jsoncomparison.exception.NotCompletedComparisonException;
import com.assignment.diff.jsoncomparison.util.JsonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Performs comparison of two stored JSON documents on demand.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/19/2019
 *
 * @author Artsiom Tsaryonau
 */
@Service
@Transactional
public class JsonComparisonResultService implements IJsonComparisonResultService {

    private final IJsonComparisonRepository repository;

    /**
     * Constructor.
     * @param repository repository
     */
    @Autowired
    public JsonComparisonResultService(IJsonComparisonRepository repository) {
        this.repository = repository;
    }

    @Override
    public JsonComparisonResult getOrPerformComparison(String comparisonId) {
        if (repository.existsById(comparisonId)) {
            JsonComparisonResult result = repository.getOne(comparisonId);
            if (ComparisonDecision.NONE == result.getDecision() && isEligibleForComparison(comparisonId, result)) {
                result = performComparison(result);
                repository.save(result);
            }
            return result;
        } else {
            throw new NoComparisonFoundException(comparisonId);
        }
    }

    private JsonComparisonResult performComparison(JsonComparisonResult result) {
        String leftSide = result.getLeftSide();
        String rightSide = result.getRightSide();
        if (leftSide.length() != rightSide.length()) {
            result.setDecision(ComparisonDecision.DIFFERENT_SIZE);
        } else if (leftSide.equals(rightSide)) {
            result.setDecision(ComparisonDecision.SAME);
        } else {
            String differences = JsonUtils.plainDiff(result.getLeftSide(), result.getRightSide());
            result.setDecision(ComparisonDecision.DIFFERENT);
            result.setDifferences(differences);
        }
        return result;
    }

    private boolean isEligibleForComparison(String comparisonId, JsonComparisonResult result) {
        if (null == result.getLeftSide() || null == result.getRightSide()) {
            Set<String> missingSides = new HashSet<>();
            if (null == result.getLeftSide()) {
                missingSides.add("left");
            }
            if (null == result.getRightSide()) {
                missingSides.add("right");
            }
            throw new NotCompletedComparisonException(comparisonId, missingSides);
        } else {
            return true;
        }
    }
}
