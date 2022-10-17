package com.example.passwordwallet.password;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PasswordNotFoundException extends RuntimeException {

    public PasswordNotFoundException() {
        super("Could not find password!");
    }
}
