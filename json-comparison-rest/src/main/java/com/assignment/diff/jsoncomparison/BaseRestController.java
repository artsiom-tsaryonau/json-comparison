package com.assignment.diff.jsoncomparison;

import com.assignment.diff.jsoncomparison.exception.NoComparisonFoundException;
import com.assignment.diff.jsoncomparison.exception.NotCompletedComparisonException;
import com.assignment.diff.jsoncomparison.exception.NotUpdatableCompleteComparisonException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Base rest controller that provides the exception handling and common method support for controllers.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/20/2019
 *
 * @author Artsiom Tsaryonau
 */
public abstract class BaseRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRestController.class);

    private static final String OK = "OK";
    private static final String ERROR = "ERROR";
    private static final String DEFAULT_ERROR_MESSAGE = "Internal server error";
    private static final String SUCCESSFULLY_STORED_MESSAGE = "Successfully stored";

    /**
     * Creates successfully stored message.
     * @return message
     */
    protected JsonResponseMessage<String> createSuccessfullyStoredMessage() {
        return new JsonResponseMessage<>(OK, SUCCESSFULLY_STORED_MESSAGE);
    }

    /**
     * Creates comparison result message.
     * @param content comparison result data
     * @return message
     */
    protected JsonResponseMessage<JsonComparisonResultMessage> createComparisonResultMessage(
        JsonComparisonResultMessage content) {
        return new JsonResponseMessage<>(OK, content);
    }

    @ExceptionHandler({
        NoComparisonFoundException.class,
        NotCompletedComparisonException.class,
        NotUpdatableCompleteComparisonException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private JsonResponseMessage<String> handleServiceException(Exception e) {
        LOGGER.error("Business error occurred.", e);
        return new JsonResponseMessage<>(ERROR, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private JsonResponseMessage<String> handleException(Exception e) {
        LOGGER.error("Internal error occurred.", e);
        return new JsonResponseMessage<>(ERROR, DEFAULT_ERROR_MESSAGE);
    }
}
