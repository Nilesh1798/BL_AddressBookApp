package com.example.AddressBook.exceptions;

import com.example.AddressBook.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(new ResponseDto("Validation Failed", errors), HttpStatus.BAD_REQUEST);
    }

    // Handle AddressBookException (Custom Exception)
    @ExceptionHandler(AddressBookException.class)
    public ResponseEntity<ResponseDto> handleAddressBookException(AddressBookException ex) {
        return new ResponseEntity<>(new ResponseDto("Address Book Error", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    // Handle Generic Exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleGlobalExceptions(Exception ex) {
        return new ResponseEntity<>(new ResponseDto("Error Occurred", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}