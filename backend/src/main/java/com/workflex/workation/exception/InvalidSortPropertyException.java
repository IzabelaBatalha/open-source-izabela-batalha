package com.workflex.workation.exception;

import java.util.Set;

public class InvalidSortPropertyException extends RuntimeException {

    private final String property;
    private final Set<String> allowedProperties;

    public InvalidSortPropertyException(String property, Set<String> allowedProperties) {
        super("Invalid sort property: " + property);
        this.property = property;
        this.allowedProperties = allowedProperties;
    }

    public String getProperty() {
        return property;
    }

    public Set<String> getAllowedProperties() {
        return allowedProperties;
    }
}
