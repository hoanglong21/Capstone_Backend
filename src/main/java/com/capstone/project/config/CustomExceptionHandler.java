package com.capstone.project.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String errorMessage = ex.getMessage();
        if (errorMessage.contains("Invalid date format")) {
            return "Date must be in the format of YYYY-MM-DD";
        }
        // add more checks for different types of errors here

        // if none of the above conditions are met, return a generic error message
        return "An error occurred while processing your request";
    }
}