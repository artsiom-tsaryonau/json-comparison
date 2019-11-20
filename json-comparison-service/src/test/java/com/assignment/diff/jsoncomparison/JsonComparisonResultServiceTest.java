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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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

    @Test
    public void testGetOrPerformComparisonWhenNoResult() {
        expect(repository.findById(COMPARISON_ID))
            .andReturn(Mono.error(new NoComparisonFoundException(COMPARISON_ID)));
        replay(repository);
        StepVerifier
            .create(comparisonResultService.getOrPerformComparison(COMPARISON_ID))
            .expectErrorMatches(throwable -> throwable instanceof NoComparisonFoundException)
            .verify();
        verify(repository);
    }

    @Test
    public void testGetOrPerformComparisonWhenSideIsBlank() {
        expect(repository.findById(COMPARISON_ID)).andReturn(Mono.just(createJsonComparisonResult()));
        replay(repository);
        StepVerifier
            .create(comparisonResultService.getOrPerformComparison(COMPARISON_ID))
            .expectErrorMatches(throwable -> throwable instanceof NotCompletedComparisonException)
            .verify();
        verify(repository);
    }

    @Test
    public void testGetOrPerformComparisonWhenDifferentLength() {
        var updated = createJsonComparisonResult(ComparisonDecision.DIFFERENT_SIZE, JSON_1, JSON_2);
        expect(repository.findById(COMPARISON_ID))
            .andReturn(Mono.just(createJsonComparisonResult(ComparisonDecision.NONE, JSON_1, JSON_2)));
        expect(repository.save(updated)).andReturn(Mono.just(updated));
        replay(repository);
        StepVerifier
            .create(comparisonResultService.getOrPerformComparison(COMPARISON_ID))
            .expectNext(updated)
            .verifyComplete();
        verify(repository);
    }

    @Test
    public void testGetOrPerformComparisonWhenSameLength() {
        var updated = createJsonComparisonResult(ComparisonDecision.DIFFERENT, JSON_1, JSON_3, JSON_3_1_DIFF);
        expect(repository.findById(COMPARISON_ID))
            .andReturn(Mono.just(createJsonComparisonResult(ComparisonDecision.NONE, JSON_1, JSON_3)));
        expect(repository.save(updated)).andReturn(Mono.just(updated));
        replay(repository);
        StepVerifier
            .create(comparisonResultService.getOrPerformComparison(COMPARISON_ID))
            .expectNext(updated)
            .verifyComplete();
        verify(repository);
    }

    @Test
    public void testGetOrPerformComparisonWhenSameLengthAndSameValue() {
        var updated = createJsonComparisonResult(ComparisonDecision.SAME, JSON_1, JSON_1);
        expect(repository.findById(COMPARISON_ID))
            .andReturn(Mono.just(createJsonComparisonResult(ComparisonDecision.NONE, JSON_1, JSON_1)));
        expect(repository.save(updated)).andReturn(Mono.just(updated));
        replay(repository);
        StepVerifier
            .create(comparisonResultService.getOrPerformComparison(COMPARISON_ID))
            .expectNext(updated)
            .verifyComplete();
        verify(repository);
    }

    private JsonComparisonResult createJsonComparisonResult() {
        var comparisonResult = new JsonComparisonResult();
        comparisonResult.setDecision(ComparisonDecision.NONE);
        comparisonResult.setComparisonId(COMPARISON_ID);
        return comparisonResult;
    }

    private JsonComparisonResult createJsonComparisonResult(ComparisonDecision decision) {
        var comparisonResult = createJsonComparisonResult();
        comparisonResult.setDecision(decision);
        return comparisonResult;
    }

    private JsonComparisonResult createJsonComparisonResult(ComparisonDecision decision, String leftSide,
                                                            String rightSide) {
        var comparisonResult = createJsonComparisonResult(decision);
        comparisonResult.setLeftSide(leftSide);
        comparisonResult.setRightSide(rightSide);
        return comparisonResult;
    }

    private JsonComparisonResult createJsonComparisonResult(ComparisonDecision decision, String leftSide,
                                                            String rightSide, String differences) {
        var comparisonResult = createJsonComparisonResult(decision, leftSide, rightSide);
        comparisonResult.setDifferences(differences);
        return comparisonResult;
    }
}
