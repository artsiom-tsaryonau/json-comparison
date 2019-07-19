package com.assignment.diff.jsoncomparison;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Response message for json comparison result.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/19/2019
 *
 * @author Artsiom Tsaryonau
 */
@JsonPropertyOrder({ "ID", "result", "differences" })
public class JsonComparisonResultMessage {
    @JsonProperty("ID")
    private final String comparisonId;
    private final String result;
    @JsonInclude(Include.NON_NULL)
    private final String differences;

    /**
     * Constructor.
     * @param comparisonId comparison id
     * @param result comparison result
     * @param differences differences between compared objects
     */
    public JsonComparisonResultMessage(String comparisonId, String result, String differences) {
        this.comparisonId = comparisonId;
        this.result = result;
        this.differences = differences;
    }

    public String getComparisonId() {
        return comparisonId;
    }

    public String getResult() {
        return result;
    }

    public String getDifferences() {
        return differences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JsonComparisonResultMessage that = (JsonComparisonResultMessage) o;

        return new EqualsBuilder()
            .append(comparisonId, that.comparisonId)
            .append(result, that.result)
            .append(differences, that.differences)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(comparisonId)
            .append(result)
            .append(differences)
            .toHashCode();
    }
}
