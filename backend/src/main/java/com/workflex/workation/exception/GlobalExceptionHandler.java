package com.workflex.workation.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidSortPropertyException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSortProperty(
            InvalidSortPropertyException ex,
            HttpServletRequest request) {

        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage() + ". Allowed values: " + String.join(", ", ex.getAllowedProperties()),
                request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(CsvImportException.class)
    public ResponseEntity<ErrorResponse> handleCsvImportException(
            CsvImportException ex,
            HttpServletRequest request) {

        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.internalServerError().body(body);
    }
}
