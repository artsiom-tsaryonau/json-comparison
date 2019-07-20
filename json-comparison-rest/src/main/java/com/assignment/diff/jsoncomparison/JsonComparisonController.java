package com.assignment.diff.jsoncomparison;

import com.assignment.diff.jsoncomparison.api.IJsonComparisonResultService;
import com.assignment.diff.jsoncomparison.api.IJsonComparisonStoringService;
import com.assignment.diff.jsoncomparison.util.Base64DecodeUtils;
import com.assignment.diff.jsoncomparison.util.JsonUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
public class JsonComparisonController extends BaseRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonComparisonController.class);

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
    public JsonResponseMessage<String> uploadLeftSide(@PathVariable("id") String comparisonId,
                                                      @RequestBody String payload) {
        LOGGER.info("Updating left side of comparison {}.", comparisonId);
        String json = Base64DecodeUtils.decode(payload);
        JsonUtils.validateJson(json);
        storingService.updateOrCreateLeftSide(comparisonId, json);
        return createSuccessfullyStoredMessage();
    }

    /**
     * Uploads right side of comparison.
     * @param comparisonId identifier of comparison case
     * @param payload base64 encoded JSON payload
     * @return result of uploading (usually OK message)
     */
    @PostMapping(value = "/v1/diff/{id}/right")
    public JsonResponseMessage<String> uploadRightSide(@PathVariable("id") String comparisonId,
                                                       @RequestBody String payload) {
        LOGGER.info("Updating right side of comparison {}.", comparisonId);
        String json = Base64DecodeUtils.decode(payload);
        JsonUtils.validateJson(json);
        storingService.updateOrCreateRightSide(comparisonId, json);
        return createSuccessfullyStoredMessage();
    }

    /**
     * Returns comparison result.
     * @param comparisonId identifier of comparison case
     * @return comparison result (or info about it not being completed)
     */
    @GetMapping("/v1/diff/{id}")
    public JsonResponseMessage<JsonComparisonResultMessage> checkComparisonResult(
        @PathVariable("id") String comparisonId) {
        LOGGER.info("Retrieving comparison result {}.", comparisonId);
        JsonComparisonResult result = comparisonService.getOrPerformComparison(comparisonId);
        JsonComparisonResultMessage comparisonMessage = new JsonComparisonResultMessage(comparisonId,
            result.getDecision().getMessage(), result.getDifferences());
        return createComparisonResultMessage(comparisonMessage);
    }
}
