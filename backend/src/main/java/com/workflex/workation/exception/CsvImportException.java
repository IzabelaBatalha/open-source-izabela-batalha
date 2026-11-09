package com.workflex.workation.exception;

public class CsvImportException extends RuntimeException {

    public CsvImportException(String message) {
        super(message);
    }

    public CsvImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
