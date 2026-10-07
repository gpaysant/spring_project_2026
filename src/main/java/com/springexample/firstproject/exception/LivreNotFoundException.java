package com.springexample.firstproject.exception;

public class LivreNotFoundException extends RuntimeException {
    public LivreNotFoundException(String message) {
        super(message);
    }
}
