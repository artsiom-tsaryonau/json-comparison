package com.assignment.diff.jsoncomparison.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.assignment.diff.jsoncomparison.exception.InvalidJsonException;
import com.fasterxml.jackson.databind.JsonNode;

import org.junit.Test;

/**
 * Tests for {@link JsonUtils}.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/20/2019
 *
 * @author Artsiom Tsaryonau
 */
public class JsonUtilsTest {
    private static final String JSON_STRING = "{\"name\":\"John\", \"surname\":\"LeBlank\"," +
        " \"address\": { \"street\": \"5th Avenue\" } }";
    private static final String INVALID_JSON_STRING = "{\"name\":\"John\", \"surname\":\"LeBlank\"," +
        " \"address\": { \"street\": 5th Avenu\" }";

    @Test
    public void testConvertToNode() {
        JsonNode node = JsonUtils.convertToNode(JSON_STRING);
        assertEquals("John", node.get("name").asText());
        assertEquals("LeBlank", node.get("surname").asText());

        assertNotNull(node.get("address"));
        assertEquals("5th Avenue", node.get("address").get("street").asText());
    }

    @Test(expected = InvalidJsonException.class)
    public void testConvertToNodeInvalidJson() {
        JsonUtils.convertToNode(INVALID_JSON_STRING);
    }

    @Test
    public void testValidateJsonValidJson() {
        JsonUtils.validateJson(JSON_STRING);
    }

    @Test(expected = InvalidJsonException.class)
    public void testValidateJsonInvalidJson() {
        JsonUtils.validateJson(INVALID_JSON_STRING);
    }
}
