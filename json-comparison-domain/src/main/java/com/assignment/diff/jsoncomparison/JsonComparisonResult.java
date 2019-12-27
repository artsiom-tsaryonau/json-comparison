package com.assignment.diff.jsoncomparison;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Result of comparison between to JSON.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/19/2019
 *
 * @author Artsiom Tsaryonau
 */
@Table("json_comparison")
public class JsonComparisonResult implements Persistable<String> {
    @Column("comparison_id")
    @Id
    private String comparisonId;
    @Column("left_side")
    private String leftSide;
    @Column("right_side")
    private String rightSide;
    // @Enumerated(EnumType.STRING) - no support for now
    @Column("decision")
    private ComparisonDecision decision;
    private String differences;

    @Transient
    private transient boolean isNewObject;

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

    public void setIsNew(boolean isNew) {
        this.isNewObject = isNew;
    }

    @Override
    public String getId() {
        return comparisonId;
    }

    @Override
    public boolean isNew() {
        return isNewObject;
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
