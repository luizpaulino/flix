package com.flix.shared.exception.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetails {
    private String message;

    public ErrorDetails(String message) {
        this.message = message;
    }
}
