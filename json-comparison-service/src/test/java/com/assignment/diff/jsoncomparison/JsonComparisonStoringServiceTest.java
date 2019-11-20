package com.assignment.diff.jsoncomparison;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import com.assignment.diff.jsoncomparison.api.IJsonComparisonStoringService;
import com.assignment.diff.jsoncomparison.exception.NotUpdatableCompleteComparisonException;

import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Tests for {@link JsonComparisonStoringService}.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/19/2019
 *
 * @author Artsiom Tsaryonau
 */
public class JsonComparisonStoringServiceTest {
    private static final String JSON = "eyJwbGFuZXQiIDogImhlYXZlbiIsInNpemUiIDogIjFrbSJ9";
    private static final String COMPARISON_ID = "comparisonId";

    private IJsonComparisonStoringService storingService;
    private IJsonComparisonRepository repository;

    @Before
    public void setUp() {
        repository = createMock(IJsonComparisonRepository.class);
        storingService = new JsonComparisonStoringService(repository);
    }

    @Test
    public void testUpdateOrCreateLeftSideWhenExist() {
        var updated = createJsonComparisonResult(ComparisonDecision.NONE, JSON, null);
        expect(repository.findById(COMPARISON_ID))
            .andReturn(Mono.just(createJsonComparisonResult(ComparisonDecision.NONE)));
        expect(repository.save(updated)).andReturn(Mono.just(updated));
        replay(repository);
        StepVerifier
            .create(storingService.updateOrCreateLeftSide(COMPARISON_ID, JSON))
            .expectNext(updated)
            .verifyComplete();
        verify(repository);
    }

    @Test
    public void testUpdateOrCreateLeftSideWhenNotExist() {
        var updated = createJsonComparisonResult(ComparisonDecision.NONE, JSON, null);
        expect(repository.findById(COMPARISON_ID)).andReturn(Mono.empty());
        expect(repository.save(createJsonComparisonResult(ComparisonDecision.NONE, JSON, null)))
            .andReturn(Mono.just(updated));
        replay(repository);
        StepVerifier
            .create(storingService.updateOrCreateLeftSide(COMPARISON_ID, JSON))
            .expectNext(updated)
            .verifyComplete();
        verify(repository);
    }

    @Test
    public void testUpdateOrCreateRightSideWhenExist() {
        var updated = createJsonComparisonResult(ComparisonDecision.NONE, null, JSON);
        expect(repository.findById(COMPARISON_ID))
            .andReturn(Mono.just(createJsonComparisonResult(ComparisonDecision.NONE)));
        expect(repository.save(updated)).andReturn(Mono.just(updated));
        replay(repository);
        StepVerifier
            .create(storingService.updateOrCreateRightSide(COMPARISON_ID, JSON))
            .expectNext(updated)
            .verifyComplete();
        verify(repository);
    }

    @Test
    public void testUpdateOrCreateRightSideWhenNotExist() {
        var updated = createJsonComparisonResult(ComparisonDecision.NONE, null, JSON);
        expect(repository.findById(COMPARISON_ID)).andReturn(Mono.empty());
        expect(repository.save(createJsonComparisonResult(ComparisonDecision.NONE, null, JSON)))
            .andReturn(Mono.just(updated));
        replay(repository);
        StepVerifier
            .create(storingService.updateOrCreateRightSide(COMPARISON_ID, JSON))
            .expectNext(updated)
            .verifyComplete();
        verify(repository);
    }

    @Test
    public void testUpdateOrCreateLeftSideWhenNotUpdatable() {
        expect(repository.findById(COMPARISON_ID))
            .andReturn(Mono.just(createJsonComparisonResult(ComparisonDecision.SAME)));
        replay(repository);
        StepVerifier
            .create(storingService.updateOrCreateLeftSide(COMPARISON_ID, JSON))
            .expectErrorMatches(throwable -> throwable instanceof NotUpdatableCompleteComparisonException)
            .verify();
        verify(repository);
    }

    @Test
    public void testUpdateOrCreateRightSideWhenNotUpdatable() {
        expect(repository.findById(COMPARISON_ID))
            .andReturn(Mono.just(createJsonComparisonResult(ComparisonDecision.SAME)));
        replay(repository);
        StepVerifier
            .create(storingService.updateOrCreateRightSide(COMPARISON_ID, JSON))
            .expectErrorMatches(throwable -> throwable instanceof NotUpdatableCompleteComparisonException)
            .verify();
        verify(repository);
    }

    private JsonComparisonResult createJsonComparisonResult(ComparisonDecision decision) {
        var result = new JsonComparisonResult();
        result.setComparisonId(COMPARISON_ID);
        result.setDecision(decision);
        return result;
    }

    private JsonComparisonResult createJsonComparisonResult(ComparisonDecision decision, String leftSide,
                                                            String rightSide) {
        var result = createJsonComparisonResult(decision);
        result.setLeftSide(leftSide);
        result.setRightSide(rightSide);
        return result;
    }
}
