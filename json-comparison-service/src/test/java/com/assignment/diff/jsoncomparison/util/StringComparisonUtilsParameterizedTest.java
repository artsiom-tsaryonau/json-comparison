package com.assignment.diff.jsoncomparison.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

/**
 * Parameterized tests for {@link StringComparisonUtils} that tests json comparison.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/20/2019
 *
 * @author Artsiom Tsaryonau
 */
@RunWith(Parameterized.class)
public class StringComparisonUtilsParameterizedTest {
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {"lefth", "right", "[startIndex=0:length=5]"},
            {"glory of rome", "glory of home", "[startIndex=9:length=1]"},
            {"glory of rome, glorious home", "glory of home, gloriaac rome",
                "[startIndex=9:length=1];[startIndex=20:length=3];[startIndex=24:length=1]"},
            {"glory of rome, glorious home", "glory of home, gloriaac romk",
                "[startIndex=9:length=1];[startIndex=20:length=3];[startIndex=24:length=1];[startIndex=27:length=1]"},
            {"glory of rome, glorious home", "glory of home, gloriaac resk",
                "[startIndex=9:length=1];[startIndex=20:length=3];[startIndex=24:length=4]"},
            {"glory of rome, glorious home", "glory of home, gloriaac rese",
                "[startIndex=9:length=1];[startIndex=20:length=3];[startIndex=24:length=3]"},
        });
    }

    private String leftComparisonSide;
    private String rightComparisonSide;
    private String differences;

    public StringComparisonUtilsParameterizedTest(String leftSide, String rightSide, String diff) {
        this.leftComparisonSide = leftSide;
        this.rightComparisonSide = rightSide;
        this.differences = diff;
    }

    @Test
    public void test() {
        assertEquals(differences, StringComparisonUtils.plainDiff(leftComparisonSide, rightComparisonSide));
    }
}
