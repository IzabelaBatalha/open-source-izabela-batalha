package com.workflex.workation.exception;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class InvalidSortPropertyExceptionTest {

    @Test
    void exposesPropertyAndAllowedValues() {
        InvalidSortPropertyException exception = new InvalidSortPropertyException(
                "foo",
                Set.of("employee", "risk"));

        assertThat(exception.getProperty()).isEqualTo("foo");
        assertThat(exception.getAllowedProperties()).containsExactlyInAnyOrder("employee", "risk");
        assertThat(exception.getMessage()).isEqualTo("Invalid sort property: foo");
    }
}
