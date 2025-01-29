package com.vault.backend.exception;

public class FieldNotUnique extends Throwable{
    public FieldNotUnique() {
        super("Field is Not Unique.");
    }
    public FieldNotUnique(String message) {
        super(String.format("%s is already saved.", message));
    }
}
