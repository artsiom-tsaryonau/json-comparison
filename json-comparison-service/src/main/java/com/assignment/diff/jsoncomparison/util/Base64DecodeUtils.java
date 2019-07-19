package com.assignment.diff.jsoncomparison.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Utility class for JSON conversion.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/19/2019
 *
 * @author Artsiom Tsaryonau
 */
public final class Base64DecodeUtils {
    private Base64DecodeUtils() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    /**
     * Performs comparison from base64 string to a regular string.
     * @param base64String string representation of base64 encoded data
     * @return decoded base64 string
     */
    public static String decode(String base64String) {
        byte[] convertedBytes = Base64.getDecoder().decode(base64String);
        return new String(convertedBytes, StandardCharsets.UTF_8);
    }
}
