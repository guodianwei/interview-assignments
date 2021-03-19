/**
 * Copyright 2019 China Renaissance Inc.
 */
package com.example.shorturl.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FieldErrorVO implements Serializable {

    private String field;

    private String message;

    public FieldErrorVO(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
