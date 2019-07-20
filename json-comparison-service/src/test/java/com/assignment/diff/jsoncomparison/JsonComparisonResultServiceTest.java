package com.assignment.diff.jsoncomparison;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import com.assignment.diff.jsoncomparison.exception.NoComparisonFoundException;
import com.assignment.diff.jsoncomparison.exception.NotCompletedComparisonException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Tests for {@link JsonComparisonResultService}.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/19/2019
 *
 * @author Artsiom Tsaryonau
 */
public class JsonComparisonResultServiceTest {
    private static final String COMPARISON_ID = "comparisonId";
    private static final String JSON_1 = "eyJwbGFuZXQiIDogImhlYXZlbiIsInNpemUiIDogIjFrbSJ9";
    private static final String JSON_2 =
        "eyJuYW1lIjoiSm9obiIsICJzdXJuYW1lIjoiTGVCbGFuayIsICJhZGRyZXNzIjogeyAic3RyZWV0IjogIjV0aCBBdmVudWUiIH0gfQ==";
    private static final String JSON_3 = "eyJwbGFuZXQiIDogImhlYXZlbiIoatbhtkUiIDogIjFrbSJ9";
    private static final String JSON_3_1_DIFF = "[startIndex=27:length=7]";

    private JsonComparisonResultService comparisonResultService;
    private IJsonComparisonRepository repository;

    @Before
    public void setUp() {
        repository = createMock(IJsonComparisonRepository.class);
        comparisonResultService = new JsonComparisonResultService(repository);
        ReflectionTestUtils.setField(comparisonResultService, "repository", repository);
    }

    @Test(expected = NoComparisonFoundException.class)
    public void testGetOrPerformComparisonWhenNoResult() {
        expect(repository.existsById(COMPARISON_ID)).andReturn(false);
        replay(repository);
        comparisonResultService.getOrPerformComparison(COMPARISON_ID);
        verify(repository);
    }

    @Test(expected = NotCompletedComparisonException.class)
    public void testGetOrPerformComparisonWhenSideIsBlank() {
        expect(repository.existsById(COMPARISON_ID)).andReturn(true);
        expect(repository.getOne(COMPARISON_ID)).andReturn(createJsonComparisonResult());
        replay(repository);
        comparisonResultService.getOrPerformComparison(COMPARISON_ID);
        verify(repository);
    }

    @Test
    public void testGetOrPerformComparisonWhenDifferentLength() {
        JsonComparisonResult updated = createJsonComparisonResult(ComparisonDecision.DIFFERENT_SIZE, JSON_1, JSON_2);
        expect(repository.existsById(COMPARISON_ID)).andReturn(true);
        expect(repository.getOne(COMPARISON_ID))
            .andReturn(createJsonComparisonResult(ComparisonDecision.NONE, JSON_1, JSON_2));
        expect(repository.save(updated)).andReturn(updated);
        replay(repository);
        comparisonResultService.getOrPerformComparison(COMPARISON_ID);
        verify(repository);
    }

    @Test
    public void testGetOrPerformComparisonWhenSameLength() {
        JsonComparisonResult updated = createJsonComparisonResult(ComparisonDecision.DIFFERENT, JSON_1, JSON_3,
            JSON_3_1_DIFF);
        expect(repository.existsById(COMPARISON_ID)).andReturn(true);
        expect(repository.getOne(COMPARISON_ID))
            .andReturn(createJsonComparisonResult(ComparisonDecision.NONE, JSON_1, JSON_3));
        expect(repository.save(updated)).andReturn(updated);
        replay(repository);
        comparisonResultService.getOrPerformComparison(COMPARISON_ID);
        verify(repository);
    }

    @Test
    public void testGetOrPerformComparisonWhenSameLengthAndSameValue() {
        JsonComparisonResult updated = createJsonComparisonResult(ComparisonDecision.SAME, JSON_1, JSON_1);
        expect(repository.existsById(COMPARISON_ID)).andReturn(true);
        expect(repository.getOne(COMPARISON_ID))
            .andReturn(createJsonComparisonResult(ComparisonDecision.NONE, JSON_1, JSON_1));
        expect(repository.save(updated)).andReturn(updated);
        replay(repository);
        comparisonResultService.getOrPerformComparison(COMPARISON_ID);
        verify(repository);
    }

    private JsonComparisonResult createJsonComparisonResult() {
        JsonComparisonResult comparisonResult = new JsonComparisonResult();
        comparisonResult.setDecision(ComparisonDecision.NONE);
        comparisonResult.setComparisonId(COMPARISON_ID);
        return comparisonResult;
    }

    private JsonComparisonResult createJsonComparisonResult(ComparisonDecision decision) {
        JsonComparisonResult comparisonResult = createJsonComparisonResult();
        comparisonResult.setDecision(decision);
        return comparisonResult;
    }

    private JsonComparisonResult createJsonComparisonResult(ComparisonDecision decision, String leftSide,
                                                            String rightSide) {
        JsonComparisonResult comparisonResult = createJsonComparisonResult(decision);
        comparisonResult.setLeftSide(leftSide);
        comparisonResult.setRightSide(rightSide);
        return comparisonResult;
    }

    private JsonComparisonResult createJsonComparisonResult(ComparisonDecision decision, String leftSide,
                                                            String rightSide, String differences) {
        JsonComparisonResult comparisonResult = createJsonComparisonResult(decision, leftSide, rightSide);
        comparisonResult.setDifferences(differences);
        return comparisonResult;
    }
}
