package com.workflex.workation.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CsvImportExceptionTest {

    @Test
    void createsExceptionWithMessage() {
        CsvImportException exception = new CsvImportException("CSV file is empty");

        assertThat(exception.getMessage()).isEqualTo("CSV file is empty");
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void createsExceptionWithMessageAndCause() {
        RuntimeException cause = new RuntimeException("io failure");
        CsvImportException exception = new CsvImportException("Failed to read CSV file", cause);

        assertThat(exception.getMessage()).isEqualTo("Failed to read CSV file");
        assertThat(exception.getCause()).isSameAs(cause);
    }
}
