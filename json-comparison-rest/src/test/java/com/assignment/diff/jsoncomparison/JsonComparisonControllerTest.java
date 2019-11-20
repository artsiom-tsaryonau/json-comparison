package com.assignment.diff.jsoncomparison;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import com.assignment.diff.jsoncomparison.api.IJsonComparisonResultService;
import com.assignment.diff.jsoncomparison.api.IJsonComparisonStoringService;
import com.assignment.diff.jsoncomparison.dto.JsonComparisonResultMessage;
import com.assignment.diff.jsoncomparison.dto.JsonResponseMessage;

import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Consumer;

/**
 * Tests for {@link JsonComparisonController}.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/20/2019
 *
 * @author Artsiom Tsaryonau
 */
public class JsonComparisonControllerTest {
    private static final ComparisonDecision COMPARISON_DECISION = ComparisonDecision.SAME;
    private static final String OK = "OK";
    private static final String STORED_MSG = "Successfully stored";
    private static final String COMPARISON_ID = "comparisonId";
    private static final String JSON_ENCODED = "eyJuYW1lIjoiSm9obiIsICJzdXJuYW1lIjoiQmxhbmsiLCAiYWRkcmVzcyI6IH" +
        "sgInN0cmVldCI6ICI1dGggQXZlbnVlIiB9IH0=";
    private JsonComparisonController controller;
    private IJsonComparisonStoringService storingService;
    private IJsonComparisonResultService comparisonService;

    @Before
    public void setUp() {
        storingService = createMock(IJsonComparisonStoringService.class);
        comparisonService = createMock(IJsonComparisonResultService.class);
        controller = new JsonComparisonController(storingService, comparisonService);
    }

    @Test
    public void testUploadLeftSide() {
        var expected = createComparisonResult(COMPARISON_ID,
            jsonComparisonResult -> jsonComparisonResult.setLeftSide(JSON_ENCODED));
        expect(storingService.updateOrCreateLeftSide(COMPARISON_ID, JSON_ENCODED)).andReturn(Mono.just(expected));
        replay(storingService, comparisonService);
        StepVerifier
            .create(controller.uploadLeftSide(COMPARISON_ID, JSON_ENCODED))
            .expectNext(new JsonResponseMessage<>(OK, STORED_MSG))
            .verifyComplete();
        verify(storingService, comparisonService);
    }

    @Test
    public void testUploadRightSide() {
        var expected = createComparisonResult(COMPARISON_ID,
            jsonComparisonResult -> jsonComparisonResult.setRightSide(JSON_ENCODED));
        expect(storingService.updateOrCreateRightSide(COMPARISON_ID, JSON_ENCODED)).andReturn(Mono.just(expected));
        expectLastCall();
        replay(storingService);
        StepVerifier.create(controller.uploadRightSide(COMPARISON_ID, JSON_ENCODED))
            .expectNext(new JsonResponseMessage<>(OK, STORED_MSG))
            .verifyComplete();
        verify(storingService);
    }

    @Test
    public void testCheckComparisonResult() {
        expect(comparisonService.getOrPerformComparison(COMPARISON_ID))
            .andReturn(Mono.just(createSameComparisonResult()));
        replay(comparisonService);
        StepVerifier
            .create(controller.checkComparisonResult(COMPARISON_ID))
            .expectNext(new JsonResponseMessage<>(OK, createResponseMessage()))
            .verifyComplete();
        verify(comparisonService);
    }

    private JsonComparisonResult createComparisonResult(String id, Consumer<JsonComparisonResult> sideUpdate) {
        var comparisonResult = new JsonComparisonResult();
        comparisonResult.setComparisonId(id);
        sideUpdate.accept(comparisonResult);
        return comparisonResult;
    }

    private JsonComparisonResult createSameComparisonResult() {
        var comparisonResult = new JsonComparisonResult();
        comparisonResult.setDecision(ComparisonDecision.SAME);
        comparisonResult.setComparisonId(COMPARISON_ID);
        return comparisonResult;
    }

    private JsonComparisonResultMessage createResponseMessage() {
         return new JsonComparisonResultMessage(COMPARISON_ID, COMPARISON_DECISION.getMessage(), null);
    }
}
