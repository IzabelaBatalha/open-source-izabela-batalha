package com.workflex.workation.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest();
        request.setRequestURI("/workflex/workation");
    }

    @Test
    void handleInvalidSortProperty_returnsBadRequestWithAllowedValues() {
        InvalidSortPropertyException exception = new InvalidSortPropertyException(
                "invalid",
                Set.of("employee", "origin"));

        ResponseEntity<ErrorResponse> response = handler.handleInvalidSortProperty(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().error()).isEqualTo("Bad Request");
        assertThat(response.getBody().message()).contains("invalid");
        assertThat(response.getBody().message()).contains("employee");
        assertThat(response.getBody().path()).isEqualTo("/workflex/workation");
    }

    @Test
    void handleCsvImportException_returnsInternalServerError() {
        CsvImportException exception = new CsvImportException("CSV file is empty");

        ResponseEntity<ErrorResponse> response = handler.handleCsvImportException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(500);
        assertThat(response.getBody().error()).isEqualTo("Internal Server Error");
        assertThat(response.getBody().message()).isEqualTo("CSV file is empty");
        assertThat(response.getBody().path()).isEqualTo("/workflex/workation");
    }
}
