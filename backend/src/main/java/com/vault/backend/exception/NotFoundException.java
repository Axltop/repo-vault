package com.vault.backend.exception;

public class NotFoundException extends Throwable{
    public NotFoundException() {
        super("Resource Not Found");
    }
}
