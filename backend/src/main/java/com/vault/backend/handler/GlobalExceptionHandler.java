package com.vault.backend.handler;

import com.vault.backend.exception.FieldNotUnique;
import com.vault.backend.exception.ResourceNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler  {

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<Object> handleExceptionNotFound(ResourceNotFound ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map(ex.getMessage()));
    }

    @ExceptionHandler(FieldNotUnique.class)
    public ResponseEntity<Object> handleExceptionNotFound(FieldNotUnique ex) {
        Map<String,String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map(ex.getMessage()));
    }

    private HashMap<String,String> map(String message){
        HashMap<String,String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }
}
