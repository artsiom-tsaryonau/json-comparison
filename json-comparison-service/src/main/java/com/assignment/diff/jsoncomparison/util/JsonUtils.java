package com.assignment.diff.jsoncomparison.util;

import com.assignment.diff.jsoncomparison.exception.InvalidJsonException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for JSON manipulation.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/19/2019
 *
 * @author Artsiom Tsaryonau
 */
public final class JsonUtils {
    private static final String DIFF_FORMAT = "[startIndex=%d:length=%d]";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonUtils() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    /**
     * Performs conversion from JSON to {@link JsonNode}.
     * @param jsonString json string
     * @return Json Node instance
     */
    public static JsonNode convertToNode(String jsonString) {
        try {
            return OBJECT_MAPPER.readTree(jsonString);
        } catch (IOException e) {
            // before storing every JSON is validated so such case is hardly possible
            throw new InvalidJsonException(e);
        }
    }

    /**
     * Checks if JSON string is valid by trying to parse it.
     * @param jsonString json to validate
     */
    public static void validateJson(String jsonString) {
        try {
            OBJECT_MAPPER.readTree(jsonString);
        } catch (IOException e) {
            throw new InvalidJsonException(e);
        }
    }

    /**
     * Plain comparison that uses the left as a reference compare it character by character with the right.
     * I assume that characters `A` and `a` are different.
     * @param left left side to compare
     * @param right right side to compare
     * @return string with differences
     */
    public static String plainDiff(String left, String right) {
        List<Pair<Integer, Integer>> differences = new ArrayList<>();
        int length = 0;
        int index = 0;
        int startIndex = -1;

        while (true) {
            char leftChar = left.charAt(index);
            char rightChar = right.charAt(index);

            if (leftChar != rightChar) {
                if (startIndex == -1) {
                    startIndex = index;
                }
                length += 1;
            } else {
                if (length != 0) {
                    differences.add(Pair.of(startIndex, length));
                    length = 0;
                    startIndex = -1;
                }
            }

            index++;
            if (index == left.length()) {
                break;
            }
        }

        /*
            Each time it finishes comparison it supposed to reset length.
            But in some cases - differences from the very beginning or in the last characters
            it exists loop. Need to manually add.
         */
        if (length != 0) {
            differences.add(Pair.of(startIndex, length));
        }

        return differences.stream()
            .map(pair -> String.format(DIFF_FORMAT, pair.getLeft(), pair.getRight()))
            .collect(Collectors.joining(";"));
    }
}
