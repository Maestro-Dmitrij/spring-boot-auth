package com.home.springbootauth.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ErrorMessage {

    private List<String> errors = new ArrayList<>();

    public ErrorMessage addError(String error) {
        errors.add(error);
        return this;
    }
}