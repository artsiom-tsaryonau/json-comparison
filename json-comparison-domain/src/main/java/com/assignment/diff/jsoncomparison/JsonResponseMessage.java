package com.assignment.diff.jsoncomparison;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Common server message.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/20/2019
 *
 * @author Artsiom Tsaryonau
 *
 * @param <T> content type in the message
 */
public class JsonResponseMessage<T> {
    private String status;
    private T content;

    /**
     * Constructor.
     * @param status response status
     * @param content content type
     */
    public JsonResponseMessage(String status, T content) {
        this.status = status;
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JsonResponseMessage<?> that = (JsonResponseMessage<?>) o;

        return new EqualsBuilder()
            .append(status, that.status)
            .append(content, that.content)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(status)
            .append(content)
            .toHashCode();
    }
}