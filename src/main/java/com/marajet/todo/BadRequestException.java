package com.marajet.todo;

public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}
