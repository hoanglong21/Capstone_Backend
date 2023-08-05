package com.capstone.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFroundException extends Exception{
    @Serial
    private static final long serialVersionUID = -7623910563032139530L;

    public ResourceNotFroundException(String message) {
        super(message);
    }
}
