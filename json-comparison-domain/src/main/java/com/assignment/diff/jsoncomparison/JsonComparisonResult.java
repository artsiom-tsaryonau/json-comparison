package com.assignment.diff.jsoncomparison;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Result of comparison between to JSON.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/19/2019
 *
 * @author Artsiom Tsaryonau
 */
@Entity
@Table(name = "json_comparison")
public class JsonComparisonResult {
    @Column(name = "comparison_id")
    @Id
    private String comparisonId;
    @Column(name = "left")
    private String leftSide;
    @Column(name = "right")
    private String rightSide;
    @Enumerated(EnumType.STRING)
    @Column(name = "decision")
    private ComparisonDecision decision;
    private String differences;

    public String getComparisonId() {
        return comparisonId;
    }

    public void setComparisonId(String comparisonId) {
        this.comparisonId = comparisonId;
    }

    public String getLeftSide() {
        return leftSide;
    }

    public void setLeftSide(String leftSide) {
        this.leftSide = leftSide;
    }

    public String getRightSide() {
        return rightSide;
    }

    public void setRightSide(String rightSide) {
        this.rightSide = rightSide;
    }

    public ComparisonDecision getDecision() {
        return decision;
    }

    public void setDecision(ComparisonDecision decision) {
        this.decision = decision;
    }

    public String getDifferences() {
        return differences;
    }

    public void setDifferences(String differences) {
        this.differences = differences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JsonComparisonResult that = (JsonComparisonResult) o;

        return new EqualsBuilder()
            .append(comparisonId, that.comparisonId)
            .append(leftSide, that.leftSide)
            .append(rightSide, that.rightSide)
            .append(decision, that.decision)
            .append(differences, that.differences)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(comparisonId)
            .append(leftSide)
            .append(rightSide)
            .append(decision)
            .append(differences)
            .toHashCode();
    }
}
