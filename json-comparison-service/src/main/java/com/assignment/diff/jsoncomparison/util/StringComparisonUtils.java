package com.assignment.diff.jsoncomparison.util;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Utility class for String manipulation.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/19/2019
 *
 * @author Artsiom Tsaryonau
 */
public final class StringComparisonUtils {
    private static final String DIFF_FORMAT = "[startIndex=%d:length=%d]";

    private StringComparisonUtils() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    /**
     * Plain comparison that uses the left as a reference compare it character by character with the right.
     * I assume that characters `A` and `a` are different.
     * @param left left side to compare
     * @param right right side to compare
     * @return string with differences
     */
    public static String plainDiff(String left, String right) {
        var differences = new ArrayList<Pair<Integer, Integer>>();
        int length = 0;
        int index = 0;
        int startIndex = -1;

        while (true) {
            var leftChar = left.charAt(index);
            var rightChar = right.charAt(index);

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
