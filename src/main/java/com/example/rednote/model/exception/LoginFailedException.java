package com.example.rednote.model.exception;

public class LoginFailedException extends RuntimeException {
    public LoginFailedException(String msg) {
        super(msg);
    }
}
