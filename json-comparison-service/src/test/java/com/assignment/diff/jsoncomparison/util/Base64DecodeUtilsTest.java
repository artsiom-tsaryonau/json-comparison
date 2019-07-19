package com.assignment.diff.jsoncomparison.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link Base64DecodeUtils}.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/20/2019
 *
 * @author Artsiom Tsaryonau
 */
public class Base64DecodeUtilsTest {
    private static final String ORIGINAL_MESSAGE = "this is message";
    private static final String ENCODED_MESSAGE = "dGhpcyBpcyBtZXNzYWdl";

    @Test
    public void testDecode() {
        assertEquals(ORIGINAL_MESSAGE, Base64DecodeUtils.decode(ENCODED_MESSAGE));
    }

}
