package com.assignment.diff.jsoncomparison;

import com.assignment.diff.jsoncomparison.api.IJsonComparisonResultService;
import com.assignment.diff.jsoncomparison.api.IJsonComparisonStoringService;
import com.assignment.diff.jsoncomparison.dto.JsonComparisonResultMessage;
import com.assignment.diff.jsoncomparison.dto.JsonResponseMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Rest controller for JSON comparison.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/19/2019
 *
 * @author Artsiom Tsaryonau
 */
@RestController
@Api("/")
public class JsonComparisonController extends BaseRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonComparisonController.class);

    private static final int OK_CODE = 200;
    private static final int ERROR_CODE = 400;

    private final IJsonComparisonStoringService storingService;
    private final IJsonComparisonResultService comparisonService;

    /**
     * Constructor.
     * @param storingService storing service
     * @param comparisonService comparison service
     */
    @Autowired
    public JsonComparisonController(IJsonComparisonStoringService storingService,
                                    IJsonComparisonResultService comparisonService) {
        this.storingService = storingService;
        this.comparisonService = comparisonService;
    }

    /**
     * Uploads left side of comparison.
     * @param comparisonId identifier of comparison case
     * @param payload base64 encoded JSON payload
     * @return result of uploading (usually OK message)
     */
    @PostMapping(value = "/v1/diff/{id}/left")
    @ApiOperation(
        value = "Upload left comparison side",
        response = JsonResponseMessage.class)
    @ApiResponses({
        @ApiResponse(code = OK_CODE, message = "OK", response = JsonResponseMessage.class),
        @ApiResponse(code = ERROR_CODE, message = "ERROR", response = JsonResponseMessage.class),
    })
    public JsonResponseMessage<String> uploadLeftSide(
            @ApiParam(required = true, name = "id", value = "comparison identifier")
            @PathVariable("id") String comparisonId,
            @ApiParam(required = true, name = "payload", value = "data to compare")
            @RequestBody String payload) {
        LOGGER.info("Updating left side of comparison {}.", comparisonId);
        storingService.updateOrCreateLeftSide(comparisonId, payload);
        return createSuccessfullyStoredMessage();
    }

    /**
     * Uploads right side of comparison.
     * @param comparisonId identifier of comparison case
     * @param payload base64 encoded JSON payload
     * @return result of uploading (usually OK message)
     */
    @PostMapping(value = "/v1/diff/{id}/right")
    @ApiOperation(
        value = "Upload right comparison side",
        response = JsonResponseMessage.class)
    @ApiResponses({
        @ApiResponse(code = OK_CODE, message = "OK", response = JsonResponseMessage.class),
        @ApiResponse(code = ERROR_CODE, message = "ERROR", response = JsonResponseMessage.class)
    })
    public JsonResponseMessage<String> uploadRightSide(
            @ApiParam(required = true, name = "id", value = "comparison identifier")
            @PathVariable("id") String comparisonId,
            @ApiParam(required = true, name = "payload", value = "data to compare")
            @RequestBody String payload) {
        LOGGER.info("Updating right side of comparison {}.", comparisonId);
        storingService.updateOrCreateRightSide(comparisonId, payload);
        return createSuccessfullyStoredMessage();
    }

    /**
     * Returns comparison result.
     * @param comparisonId identifier of comparison case
     * @return comparison result (or info about it not being completed)
     */
    @GetMapping("/v1/diff/{id}")
    @ApiOperation(
        value = "Returns comparison result",
        notes = "Returns existing comparison result or performs comparison if it's not done",
        response = JsonResponseMessage.class)
    @ApiResponses({
        @ApiResponse(code = OK_CODE, message = "OK", response = JsonResponseMessage.class),
        @ApiResponse(code = ERROR_CODE, message = "ERROR", response = JsonResponseMessage.class)
    })
    public JsonResponseMessage<JsonComparisonResultMessage> checkComparisonResult(
            @ApiParam(required = true, name = "id", value = "comparison identifier")
            @PathVariable("id") String comparisonId) {
        LOGGER.info("Retrieving comparison result {}.", comparisonId);
        JsonComparisonResult result = comparisonService.getOrPerformComparison(comparisonId);
        JsonComparisonResultMessage comparisonMessage = new JsonComparisonResultMessage(comparisonId,
            result.getDecision().getMessage(), result.getDifferences());
        return createComparisonResultMessage(comparisonMessage);
    }
}
