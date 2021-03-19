/**
 * Copyright 2019 China Renaissance Inc.
 */
package com.example.shorturl.vo;



import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ErrorVO implements Serializable {

    private final String message;
    private final String description;

    private List<FieldErrorVO> fieldErrors;

    public ErrorVO(String message) {
        this(message, null);
    }

    public ErrorVO(String message, String description) {
        this.message = message;
        this.description = description;
    }

    public ErrorVO(String message, String description, List<FieldErrorVO> fieldErrors) {
        this.message = message;
        this.description = description;
        this.fieldErrors = fieldErrors;
    }

    public void add(String field, String message) {
        if (fieldErrors == null) {
            fieldErrors = new ArrayList<>();
        }
        fieldErrors.add(new FieldErrorVO(field, message));
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    public List<FieldErrorVO> getFieldErrors() {
        return fieldErrors;
    }
}
