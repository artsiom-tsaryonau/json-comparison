package com.assignment.diff.jsoncomparison;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import com.assignment.diff.jsoncomparison.api.IJsonComparisonResultService;
import com.assignment.diff.jsoncomparison.api.IJsonComparisonStoringService;

import org.junit.Before;
import org.junit.Test;

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
    private static final String JSON_DECODED = "{\"name\":\"John\", \"surname\":\"Blank\", \"address\": " +
        "{ \"street\": \"5th Avenue\" } }";
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
        storingService.updateOrCreateLeftSide(COMPARISON_ID, JSON_DECODED);
        expectLastCall();
        replay(storingService, comparisonService);
        JsonResponseMessage<String> responseMessage = controller.uploadLeftSide(COMPARISON_ID, JSON_ENCODED);
        assertEquals(new JsonResponseMessage<>(OK, STORED_MSG), responseMessage);
        verify(storingService, comparisonService);
    }

    @Test
    public void testUploadRightSide() {
        storingService.updateOrCreateRightSide(COMPARISON_ID, JSON_DECODED);
        expectLastCall();
        replay(storingService);
        JsonResponseMessage<String> responseMessage = controller.uploadRightSide(COMPARISON_ID, JSON_ENCODED);
        assertEquals(new JsonResponseMessage<>(OK, STORED_MSG), responseMessage);
        verify(storingService);
    }

    @Test
    public void testCheckComparisonResult() {
        expect(comparisonService.getOrPerformComparison(COMPARISON_ID)).andReturn(createSameComparisonResult());
        replay(comparisonService);
        JsonResponseMessage<JsonComparisonResultMessage> responseMessage =
            controller.checkComparisonResult(COMPARISON_ID);
        assertEquals(new JsonResponseMessage<JsonComparisonResultMessage>(OK, createResponseMessage()), responseMessage);
        verify(comparisonService);
    }

    private JsonComparisonResult createSameComparisonResult() {
        JsonComparisonResult comparisonResult = new JsonComparisonResult();
        comparisonResult.setDecision(ComparisonDecision.SAME);
        comparisonResult.setComparisonId(COMPARISON_ID);
        return comparisonResult;
    }

    private JsonComparisonResultMessage createResponseMessage() {
         return new JsonComparisonResultMessage(COMPARISON_ID, COMPARISON_DECISION.getMessage(), null);
    }
}
